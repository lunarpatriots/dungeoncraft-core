package com.lunarpatriots.dungeoncraft.common.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ClientConfig {
  private String username;
  private String password;

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("username", this.getUsername());
    map.put("password", this.getPassword());

    return map;
  }
}
