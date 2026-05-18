package com.lunarpatriots.dungeoncraft.server.modules.serverauth.service;

import com.lunarpatriots.dungeoncraft.server.modules.serverauth.exceptions.UserAuthException;
import com.lunarpatriots.dungeoncraft.server.modules.serverauth.model.DatabaseProperties;
import com.lunarpatriots.dungeoncraft.server.modules.serverauth.model.ServerAuth;
import com.lunarpatriots.dungeoncraft.server.modules.serverauth.model.UserInfo;
import com.lunarpatriots.dungeoncraft.server.modules.serverauth.util.UserAuthUtil;
import com.lunarpatriots.dungeoncraft.server.util.DbConnectionUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.lunarpatriots.dungeoncraft.common.constants.NetworkingConstants.MOD_HANDSHAKE_PACKET_ID;

public class ServerAuthService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerAuthService.class);

  private ServerAuthService() {
  }

  public static void triggerServerAuth(final ServerAuth serverAuth) {
    if (serverAuth.getEnabled()) {
      LOGGER.info("Starting ServerAuth module.");
      final DatabaseProperties dbProperties = serverAuth.getDatabase();
      DbConnectionUtil.initDbConnection(dbProperties);

      ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {

        final PacketByteBuf buf = PacketByteBufs.create();

        sender.sendPacket(MOD_HANDSHAKE_PACKET_ID, buf);
      });

      ServerLoginNetworking.registerGlobalReceiver(MOD_HANDSHAKE_PACKET_ID,
        (server, handler, understood, payload, synchronizer, responseSender) -> {
          if (understood) {
            final String username = payload.readString(32767);
            final String password = payload.readString(32767);

            if (validateClientData(username, password)) {
              try {
                final UserInfo userInfo = UserAuthUtil.getUserInfo(username);
                if (null != userInfo) {
                  final String storedHash = userInfo.getPasswordHash();

                  if (UserAuthUtil.verifyCredentials(password, storedHash)) {
                    if (!userInfo.isActive()) {
                      handler.disconnect(Text.literal("User is not whitelisted!"));
                    }
                  } else {
                    throw new UserAuthException("Password is incorrect!");
                  }
                } else {
                  if (serverAuth.getRegistrationAllowed()) {
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
    } else {
      LOGGER.info("ServerAuth module disabled. Skipping module...");
    }
  }

  private static boolean validateClientData(final String username, final String password) {
    return StringUtils.isNotBlank(username)
      && StringUtils.isNotBlank(password)
      && username.length() <= 255
      && password.length() <= 255;
  }
}
