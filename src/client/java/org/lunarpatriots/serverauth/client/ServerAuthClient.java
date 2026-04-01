package org.lunarpatriots.serverauth.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import org.lunarpatriots.serverauth.client.model.ClientConfig;
import org.lunarpatriots.serverauth.client.util.ConfigLoaderUtil;
import org.lunarpatriots.serverauth.common.util.HashingUtil;

import java.util.concurrent.CompletableFuture;

import static org.lunarpatriots.serverauth.common.constants.NetworkingConstants.MOD_HANDSHAKE_PACKET_ID;

public class ServerAuthClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientLoginNetworking.registerGlobalReceiver(MOD_HANDSHAKE_PACKET_ID, (client, handler, buf, responseSender) -> {
      final String nonce = buf.readString(32767);

      final ClientConfig clientConfig = ConfigLoaderUtil.loadClientConfig();

      final String username = clientConfig.getUsername();
      final String password = clientConfig.getPassword();

      final PacketByteBuf response = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
      response.writeString(username);
      response.writeString(password);
      response.writeString(HashingUtil.sha256(password + nonce));

      return CompletableFuture.completedFuture(response);
    });
  }
}
