package com.lunarpatriots.dungeoncraft.common.util;

import com.lunarpatriots.dungeoncraft.common.constants.AppConstants;
import com.lunarpatriots.dungeoncraft.common.model.ClientConfig;
import com.lunarpatriots.dungeoncraft.server.model.ServerConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ConfigManager {

  private static final Path DIR = FabricLoader.getInstance().getConfigDir();
  public static final Path CLIENT_CONFIG_PATH = DIR.resolve(AppConstants.CLIENT_CONFIG_FILE);
  public static final Path SERVER_CONFIG_PATH = DIR.resolve(AppConstants.SERVER_CONFIG_FILE);

  private static Yaml createYaml(final Class<?> clazz) {
    final DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    dumperOptions.setPrettyFlow(true);

    final Representer representer = new Representer(dumperOptions) {{
        this.nullRepresenter = data -> representScalar(Tag.NULL, StringUtils.EMPTY);

        this.addClassTag(clazz, Tag.MAP);
      }
    };

    return new Yaml(representer, dumperOptions);
  }

  public static <T> T loadConfigs(final Class <T> clazz,
                                  final T defaultValue,
                                  final Predicate<T> validator) {

    final Path path = clazz == ServerConfig.class
      ? SERVER_CONFIG_PATH
      : CLIENT_CONFIG_PATH;

    validateOrCreateFile(clazz, path, defaultValue);

    T returnValue;
    try (Reader reader = Files.newBufferedReader(path)) {
      returnValue = createYaml(clazz).loadAs(reader, clazz);

      if (returnValue == null || !validator.test(returnValue)) {
        save (clazz, defaultValue);
        returnValue = defaultValue;
      }

    } catch (final Exception ex) {
      save (clazz, defaultValue);
      returnValue = defaultValue;
    }

    return returnValue;
  }

  private static <T> void validateOrCreateFile(final Class<?> clazz, final Path path, final T defaultValue) {
    try {
      if (Files.notExists(path)) {
        Files.createDirectories(path.getParent());
        save(clazz, defaultValue);
      }
    } catch (final Exception ex) {
      throw new RuntimeException("Failed to create config file: " + path, ex);
    }
  }

  public static void save(final Class<?> clazz, final Object config) {
    final Path path = clazz == ServerConfig.class
      ? SERVER_CONFIG_PATH
      : CLIENT_CONFIG_PATH;

    final Map<String, Object> yamlMap;

    if (config instanceof ServerConfig serverConfig) {
      yamlMap = serverConfig.toMap();
    } else if (config instanceof ClientConfig clientConfig) {
      yamlMap = clientConfig.toMap();
    } else {
      yamlMap = new LinkedHashMap<>();
    }

    try (final Writer writer = Files.newBufferedWriter(path)) {
      createYaml(clazz).dump(yamlMap, writer);
    } catch (final Exception ex) {
      throw new RuntimeException("Failed to save config file: " + path, ex);
    }
  }
}
