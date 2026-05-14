package com.lunarpatriots.dungeoncraft.server.validator;

import com.lunarpatriots.dungeoncraft.server.model.ServerConfig;
import org.apache.commons.lang3.StringUtils;

public interface ServerConfigValidator {

  static boolean validate(final ServerConfig serverConfig) {
    return null != serverConfig
      && null != serverConfig.getServerAuth()
      && null != serverConfig.getServerAuth().getEnabled()
      && (!serverConfig.getServerAuth().getEnabled()
        || (null != serverConfig.getServerAuth().getDatabase()
          && StringUtils.isNotBlank(serverConfig.getServerAuth().getDatabase().getJdbcUrl())
          && StringUtils.isNotBlank(serverConfig.getServerAuth().getDatabase().getUsername())
          && StringUtils.isNotBlank(serverConfig.getServerAuth().getDatabase().getPassword())
          && null != serverConfig.getServerAuth().getDatabase().getPoolSize()
          && null != serverConfig.getServerAuth().getRegistrationAllowed()));
  }
}
