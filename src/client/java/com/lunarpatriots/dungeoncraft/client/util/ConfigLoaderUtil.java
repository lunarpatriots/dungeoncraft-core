package com.lunarpatriots.dungeoncraft.client.util;

import com.lunarpatriots.dungeoncraft.common.exceptions.ConfigFileException;
import com.mysql.cj.util.StringUtils;
import net.fabricmc.loader.api.FabricLoader;
import com.lunarpatriots.dungeoncraft.client.model.ClientConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoaderUtil {

  private static ClientConfig clientConfig;

  public static ClientConfig loadClientConfig() {
    if (clientConfig == null) {
      final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("dungeoncraft-client.yml");
      final Yaml yaml = new Yaml();

      if (Files.exists(configPath)) {
        try (final InputStream inputStream = Files.newInputStream(configPath)) {
          clientConfig = yaml.loadAs(inputStream, ClientConfig.class);

          if (!validateClientConfigs(clientConfig)) {
            throw new ConfigFileException("There was an error reading the config file!");
          }
        } catch (final IOException | ConfigFileException ex) {
          throw new RuntimeException("Config file missing!", ex);
        }
      } else {
        clientConfig = new ClientConfig();

        try {
          Files.createDirectories(configPath.getParent());

          try (InputStream inputStream = com.lunarpatriots.dungeoncraft.server.util.ConfigLoaderUtil.class.getClassLoader()
            .getResourceAsStream("dungeoncraft-client.yml")) {
            if (inputStream != null) {
              Files.copy(inputStream, configPath);
            } else {
              throw new RuntimeException("Unable to locate config file template!");
            }
          }
        } catch (final IOException ex) {
          throw new RuntimeException("Failed to generate config file!", ex);
        }
      }
    }

    return clientConfig;
  }

  public static boolean validateClientConfigs(final ClientConfig config) {
    return config != null
      && !StringUtils.isNullOrEmpty(config.getUsername())
      && !StringUtils.isNullOrEmpty(config.getPassword());
  }
}
