package com.lunarpatriots.dungeoncraft.server.util;

import com.lunarpatriots.dungeoncraft.server.model.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(DbConnectionUtil.class);

  private static HikariDataSource dbConnection;

  public static void initDbConnection(final DatabaseProperties databaseProperties) {
    final HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setJdbcUrl(databaseProperties.getJdbcUrl());
    hikariConfig.setUsername(databaseProperties.getUsername());
    hikariConfig.setPassword(databaseProperties.getPassword());

    hikariConfig.setMinimumIdle(2);
    hikariConfig.setMaximumPoolSize(databaseProperties.getPoolSize());
    hikariConfig.setIdleTimeout(30000);
    hikariConfig.setMaxLifetime(1800000);
    hikariConfig.setConnectionTimeout(5000);

    hikariConfig.addDataSourceProperty("cachePrepStmts", true);
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    dbConnection = new HikariDataSource(hikariConfig);
  }

  public static Connection getConnection() throws SQLException {
    if (dbConnection == null) {
      throw new IllegalStateException("Database not initialized!");
    } else {
      LOGGER.info("Starting database connection");
      return dbConnection.getConnection();
    }
  }

  private DbConnectionUtil() {
  }
}
