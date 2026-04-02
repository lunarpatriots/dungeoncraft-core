package org.lunarpatriots.serverauth.server.util;

import com.mysql.cj.util.StringUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.lunarpatriots.serverauth.common.exceptions.ConfigFileException;
import org.lunarpatriots.serverauth.server.model.ServerConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoaderUtil {

  private static ServerConfig serverConfig;

  public static ServerConfig loadServerConfigs() {
    if (serverConfig == null) {
      final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("serverauth-server.yml");
      final Yaml yaml = new Yaml();

      if (Files.exists(configPath)) {
        try (final InputStream inputStream = Files.newInputStream(configPath)) {
          serverConfig = yaml.loadAs(inputStream, ServerConfig.class);

          if (!validateServerConfigs(serverConfig)) {
            throw new ConfigFileException("There was an error reading the config file!");
          }
        } catch (final IOException | ConfigFileException ex) {
          throw new RuntimeException("Config file missing!", ex);
        }
      } else {
        serverConfig = new ServerConfig();

        try {
          Files.createDirectories(configPath.getParent());

          try (Writer writer = Files.newBufferedWriter(configPath)) {
            yaml.dump(serverConfig, writer);
          }
        } catch (final IOException ex) {
          throw new RuntimeException("Failed to generate default config file!", ex);
        }
      }
    }

    return serverConfig;
  }

  public static boolean validateServerConfigs(final ServerConfig serverConfig) {
    return serverConfig != null
      && serverConfig.getEnabled() != null
      && (!serverConfig.getEnabled()
        || (serverConfig.getDatabase() != null
          && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getJdbcUrl())
          && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getUsername())
          && !StringUtils.isNullOrEmpty(serverConfig.getDatabase().getPassword())
          && serverConfig.getDatabase().getPoolSize() != null
          && serverConfig.getRegistrationAllowed() != null));
  }

  private ConfigLoaderUtil() {
  }
}
