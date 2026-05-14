package com.lunarpatriots.dungeoncraft.server.model;

import com.lunarpatriots.dungeoncraft.server.modules.serverauth.model.ServerAuth;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ServerConfig {
  private ServerAuth serverAuth = new ServerAuth();

  public Map<String, Object> toMap() {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put("serverAuth", this.getServerAuth().toMap());

    return map;
  }
}
