package com.lunarpatriots.dungeoncraft.server.modules.serverauth.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DatabaseProperties {
  private String jdbcUrl;
  private String username;
  private String password;
  private Integer poolSize = 10;

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("jdbcUrl", this.getJdbcUrl());
    map.put("username", this.getUsername());
    map.put("password", this.getPassword());
    map.put("poolSize", this.getPoolSize());

    return map;
  }
}
