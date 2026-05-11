package com.lunarpatriots.dungeoncraft.client.validator;

import com.lunarpatriots.dungeoncraft.common.model.ClientConfig;
import com.mysql.cj.util.StringUtils;

public interface ClientConfigValidator {

  static boolean validate(final ClientConfig config) {
    return config != null
      && !StringUtils.isNullOrEmpty(config.getUsername())
      && !StringUtils.isNullOrEmpty(config.getPassword());
  }
}
