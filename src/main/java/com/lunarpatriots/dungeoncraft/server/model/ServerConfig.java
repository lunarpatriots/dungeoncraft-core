package com.lunarpatriots.dungeoncraft.server.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ServerConfig {
  private DatabaseProperties database = new DatabaseProperties();
  private Boolean registrationAllowed = false;
  private Boolean enabled = false;

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("database", this.getDatabase().toMap());
    map.put("registrationAllowed", this.getRegistrationAllowed());
    map.put("enabled", this.getEnabled());

    return map;
  }
}
