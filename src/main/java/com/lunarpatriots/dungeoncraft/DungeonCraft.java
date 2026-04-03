package com.lunarpatriots.dungeoncraft;

import com.lunarpatriots.dungeoncraft.server.model.DatabaseProperties;
import com.lunarpatriots.dungeoncraft.server.util.ConfigLoaderUtil;
import com.lunarpatriots.dungeoncraft.server.util.UserAuthUtil;
import com.mysql.cj.util.StringUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import com.lunarpatriots.dungeoncraft.common.util.HashingUtil;
import com.lunarpatriots.dungeoncraft.server.exceptions.UserAuthException;
import com.lunarpatriots.dungeoncraft.server.model.ServerConfig;
import com.lunarpatriots.dungeoncraft.server.model.UserInfo;
import com.lunarpatriots.dungeoncraft.server.util.DbConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.lunarpatriots.dungeoncraft.common.constants.NetworkingConstants.MOD_HANDSHAKE_PACKET_ID;

public class DungeonCraft implements ModInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DungeonCraft.class);
  private static final Map<ServerLoginNetworkHandler, String> loginNonces = new HashMap<>();

  @Override
  public void onInitialize() {
    LOGGER.info("Starting DungeonCraft");
    final ServerConfig serverConfig = ConfigLoaderUtil.loadServerConfigs();

    if (serverConfig.getEnabled()) {
      final DatabaseProperties dbProperties = serverConfig.getDatabase();
      DbConnectionUtil.initDbConnection(dbProperties);

      ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
        final String nonce = HashingUtil.generateNonce(16);
        loginNonces.put(handler, nonce);

        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(nonce);

        sender.sendPacket(MOD_HANDSHAKE_PACKET_ID, buf);
      });

      ServerLoginNetworking.registerGlobalReceiver(MOD_HANDSHAKE_PACKET_ID,
        (server, handler, understood, payload, synchronizer, responseSender) -> {
          if (understood) {
            final String username = payload.readString(32767);
            final String password = payload.readString(32767);
            final String nonceHash = payload.readString(32767);

            final String nonce = loginNonces.remove(handler);

            if (validateClientData(username, password, nonceHash)) {
              try {
                final UserInfo userInfo = UserAuthUtil.getUserInfo(username);
                if (userInfo != null) {
                  final String storedHash = userInfo.getPasswordHash();

                  if (UserAuthUtil.verifyCredentials(password, storedHash, nonceHash, nonce)) {
                    if (!userInfo.isActive()) {
                      handler.disconnect(Text.literal("User is not whitelisted!"));
                    }
                  } else {
                    throw new UserAuthException("Password is incorrect!");
                  }
                } else {
                  if (serverConfig.getRegistrationAllowed()) {
                    UserAuthUtil.registerUser(username, password);
                    handler.disconnect(Text.literal("Thank you for registering! Please wait to be whitelisted."));
                  } else {
                    throw new UserAuthException("User is not registered!");
                  }
                }
              } catch (final UserAuthException ex) {
                LOGGER.error("Authentication error: ", ex);
                handler.disconnect(Text.literal(ex.getMessage()));
              } catch (final SQLException ex) {
                handler.disconnect(Text.literal("Authentication error!"));
                LOGGER.error("SQL Error: ", ex);
              }
            } else {
              handler.disconnect(Text.literal("Unable to authenticate player!"));
            }
          } else {
            handler.disconnect(Text.literal("Authentication mod required!"));
          }
        });
    }
  }

  private boolean validateClientData(final String username,
                                     final String password,
                                     final String nonceHash) {
    return !StringUtils.isNullOrEmpty(username)
      && !StringUtils.isNullOrEmpty(password)
      && !StringUtils.isNullOrEmpty(nonceHash);
  }
}
