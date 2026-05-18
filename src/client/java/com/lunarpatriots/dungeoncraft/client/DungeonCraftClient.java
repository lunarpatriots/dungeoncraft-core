package com.lunarpatriots.dungeoncraft.client;

import com.lunarpatriots.dungeoncraft.client.validator.ClientConfigValidator;
import com.lunarpatriots.dungeoncraft.common.util.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import com.lunarpatriots.dungeoncraft.common.model.ClientConfig;
import com.lunarpatriots.dungeoncraft.common.util.HashingUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;

import static com.lunarpatriots.dungeoncraft.common.constants.NetworkingConstants.MOD_HANDSHAKE_PACKET_ID;

public class DungeonCraftClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    ClientLoginNetworking.registerGlobalReceiver(MOD_HANDSHAKE_PACKET_ID, (client, handler, buf, responseSender) -> {
      final ClientConfig clientConfig = ConfigManager.loadConfigs(
        ClientConfig.class,
        new ClientConfig(),
        ClientConfigValidator::validate);

      final String nonce = buf.readString(32767);

      final String username = StringUtils.isNotBlank(clientConfig.getUsername())
        ? clientConfig.getUsername()
        : StringUtils.EMPTY;
      final String password = StringUtils.isNotBlank(clientConfig.getPassword())
        ? clientConfig.getUsername()
        : StringUtils.EMPTY;

      final PacketByteBuf response = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
      response.writeString(username);
      response.writeString(password);
      response.writeString(HashingUtil.sha256(password + nonce));

      return CompletableFuture.completedFuture(response);
    });
  }
}
