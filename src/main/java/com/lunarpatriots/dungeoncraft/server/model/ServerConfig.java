package com.lunarpatriots.dungeoncraft.server.model;

import lombok.Data;

@Data
public class ServerConfig {
  private DatabaseProperties database;
  private Boolean registrationAllowed;
  private Boolean enabled = false;
}
