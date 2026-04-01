package org.lunarpatriots.serverauth.server.model;

import lombok.Data;

@Data
public class DatabaseProperties {
  private String jdbcUrl;
  private String username;
  private String password;
  private Integer poolSize;
}
