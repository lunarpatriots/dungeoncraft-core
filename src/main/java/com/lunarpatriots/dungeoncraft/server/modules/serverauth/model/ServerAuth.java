package com.lunarpatriots.dungeoncraft.server.modules.serverauth.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ServerAuth {
  private Boolean enabled = false;
  private Boolean registrationAllowed = false;
  private DatabaseProperties database = new DatabaseProperties();

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("enabled", this.getEnabled());
    map.put("registrationAllowed", this.getRegistrationAllowed());
    map.put("database", this.getDatabase().toMap());

    return map;
  }
}
