package com.lunarpatriots.dungeoncraft.server.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.lunarpatriots.dungeoncraft.common.util.HashingUtil;
import com.lunarpatriots.dungeoncraft.server.model.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.lunarpatriots.dungeoncraft.common.constants.SqlConstants.GET_USER_INFO;
import static com.lunarpatriots.dungeoncraft.common.constants.SqlConstants.REGISTER_USER;

public class UserAuthUtil {

  public static void registerUser(final String username, final String password) throws SQLException {
    final Connection dbConnection = DbConnectionUtil.getConnection();

    final String hashedPw = BCrypt.withDefaults().hashToString(12, password.toCharArray());

    try (PreparedStatement preparedStatement = buildPreparedStatement(dbConnection, REGISTER_USER, username, hashedPw)) {
      preparedStatement.executeUpdate();
    }
  }

  public static UserInfo getUserInfo(final String username) throws SQLException {
    final Connection dbConnection = DbConnectionUtil.getConnection();

    try (PreparedStatement preparedStatement = buildPreparedStatement(dbConnection, GET_USER_INFO, username);
         ResultSet resultSet = preparedStatement.executeQuery()) {

      UserInfo userInfo = null;
      if (resultSet.next()) {
        userInfo = new UserInfo();
        userInfo.setPasswordHash(resultSet.getString("password_hash"));
        userInfo.setActive(resultSet.getBoolean("is_active"));
      }

      return userInfo;
    }
  }

  public static boolean verifyCredentials(final String password,
                                          final String storedHash,
                                          final String nonceHash,
                                          final String nonce) throws SQLException {
    final String expectedHash = HashingUtil.sha256(password + nonce);

    return BCrypt.verifyer().verify((password).toCharArray(), storedHash).verified
      && expectedHash.equals(nonceHash);
  }

  private static PreparedStatement buildPreparedStatement(final Connection dbConnection,
                                                          final String query,
                                                          final String username) throws SQLException {
    final PreparedStatement statement =  dbConnection.prepareStatement(query);
    statement.setString(1, username);

    return statement;
  }

  private static PreparedStatement buildPreparedStatement(final Connection dbConnection,
                                                          final String query,
                                                          final String username,
                                                          final String password) throws SQLException {
    final PreparedStatement statement =  dbConnection.prepareStatement(query);
    statement.setString(1, username);
    statement.setString(2, password);

    return statement;
  }

  private UserAuthUtil() {
  }
}
