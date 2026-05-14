package com.lunarpatriots.dungeoncraft.server.modules.serverauth.model;

import lombok.Data;

@Data
public class UserInfo {
  private String username;
  private String passwordHash;
  private boolean isActive;
}
