package com.lunarpatriots.dungeoncraft.server.validator;

import com.lunarpatriots.dungeoncraft.server.model.ServerConfig;
import com.mysql.cj.util.StringUtils;

public interface ServerConfigValidator {

  static boolean validate(final ServerConfig serverConfig) {
    return serverConfig != null
      && serverConfig.getEnabled() != null
      && (!serverConfig.getEnabled()
      || (serverConfig.getDatabase() != null
      && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getJdbcUrl())
      && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getUsername())
      && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getPassword())
      && serverConfig.getDatabase().getPoolSize() != null
      && serverConfig.getRegistrationAllowed() != null));
  }
}
