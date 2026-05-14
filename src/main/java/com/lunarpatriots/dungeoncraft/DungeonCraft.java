package com.lunarpatriots.dungeoncraft;

import com.lunarpatriots.dungeoncraft.common.util.ConfigManager;
import com.lunarpatriots.dungeoncraft.server.model.ServerConfig;
import com.lunarpatriots.dungeoncraft.server.modules.serverauth.service.ServerAuthService;
import com.lunarpatriots.dungeoncraft.server.validator.ServerConfigValidator;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DungeonCraft implements ModInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DungeonCraft.class);

  @Override
  public void onInitialize() {
    LOGGER.info("Starting DungeonCraft");
    final ServerConfig serverConfig = ConfigManager.loadConfigs(
      ServerConfig.class,
      new ServerConfig(),
      ServerConfigValidator::validate);

    if (null != serverConfig) {
      ServerAuthService.triggerServerAuth(serverConfig.getServerAuth());
    }
  }
}
