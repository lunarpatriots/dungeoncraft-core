package com.lunarpatriots.dungeoncraft.server.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DatabaseProperties {
  private String jdbcUrl = StringUtils.EMPTY;
  private String username = StringUtils.EMPTY;
  private String password = StringUtils.EMPTY;
  private Integer poolSize = 10;
}
