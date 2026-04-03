package com.lunarpatriots.dungeoncraft.server.model;

import lombok.Data;

@Data
public class UserInfo {
  private String username;
  private String passwordHash;
  private boolean isActive;
}
