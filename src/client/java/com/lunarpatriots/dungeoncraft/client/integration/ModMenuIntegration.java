package com.lunarpatriots.dungeoncraft.client.integration;

import com.lunarpatriots.dungeoncraft.client.validator.ClientConfigValidator;
import com.lunarpatriots.dungeoncraft.common.model.ClientConfig;
import com.lunarpatriots.dungeoncraft.common.util.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public class ModMenuIntegration implements ModMenuApi {

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    final ClientConfig clientConfig = ConfigManager.loadConfigs(
      ClientConfig.class,
      new ClientConfig(),
      ClientConfigValidator::validate);

    return parentScreen -> {
      final ConfigBuilder configBuilder = ConfigBuilder.create()
        .setParentScreen(parentScreen)
        .setTitle(Text.literal("dungeoncraft-client.yml"));

      final ConfigCategory clientConfigs = configBuilder.getOrCreateCategory(Text.literal("Client Configs"));

      clientConfigs.addEntry(ConfigEntryBuilder.create()
        .startTextDescription(Text.literal("Registration/Whitelisting")).build());

      clientConfigs.addEntry(ConfigEntryBuilder.create()
        .startStrField(
          Text.literal("Username"),
          StringUtils.isNotEmpty(clientConfig.getUsername())
            ? clientConfig.getUsername()
            : StringUtils.EMPTY)
        .setDefaultValue(StringUtils.EMPTY)
        .setSaveConsumer(clientConfig::setUsername)
        .build());

      clientConfigs.addEntry(new PasswordFieldEntry(Text.literal("Password"), clientConfig.getPassword())
        .setSaveConsumer(clientConfig::setPassword));

      return configBuilder
        .setSavingRunnable(() -> ConfigManager.save(ClientConfig.class,clientConfig))
        .build();
    };
  }
}
