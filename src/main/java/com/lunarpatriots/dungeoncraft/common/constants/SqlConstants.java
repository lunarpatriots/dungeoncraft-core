package com.lunarpatriots.dungeoncraft.common.constants;

public class SqlConstants {

  public static final String REGISTER_USER= "INSERT INTO auth_table (username, password_hash) VALUES(?,?)";
  public static final String GET_USER_INFO = "SELECT password_hash, is_active FROM auth_table WHERE username = ?";

  private SqlConstants() {

  }
}
