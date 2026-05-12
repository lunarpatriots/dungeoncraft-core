package com.lunarpatriots.dungeoncraft.client.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PasswordFieldEntry extends AbstractConfigListEntry<String> {

  private final PasswordTextFieldWidget textField;

  private final String defaultValue;

  private Consumer<String> saveConsumer = s -> {};

  public PasswordFieldEntry(
    Text fieldName,
    String value
  ) {
    super(fieldName, false);

    this.defaultValue = value == null ? "" : value;

    this.textField = new PasswordTextFieldWidget(
      MinecraftClient.getInstance().textRenderer,
      0,
      0,
      150,
      20,
      fieldName
    );

    this.textField.setText(this.defaultValue);
  }

  public PasswordFieldEntry setSaveConsumer(
    Consumer<String> saveConsumer
  ) {
    this.saveConsumer = saveConsumer;
    return this;
  }

  @Override
  public String getValue() {
    return this.textField.getText();
  }

  @Override
  public Optional<Text> getError() {
    return Optional.empty();
  }

  @Override
  public void save() {
    this.saveConsumer.accept(this.textField.getText());
  }

  @Override
  public Optional<String> getDefaultValue() {
    return Optional.of(this.defaultValue);
  }

  @Override
  public boolean isEdited() {
    return !this.getValue().equals(this.defaultValue);
  }

  @Override
  public void render(
    DrawContext context,
    int index,
    int y,
    int x,
    int entryWidth,
    int entryHeight,
    int mouseX,
    int mouseY,
    boolean hovered,
    float delta
  ) {
    MinecraftClient client = MinecraftClient.getInstance();

    context.drawText(
      client.textRenderer,
      this.getFieldName(),
      x,
      y + 6,
      0xFFFFFF,
      true
    );

    this.textField.setX(x + entryWidth - 150);
    this.textField.setY(y);

    this.textField.render(context, mouseX, mouseY, delta);
  }

  @Override
  public List<? extends Selectable> narratables() {
    return List.of(this.textField);
  }

  @Override
  public List<? extends Element> children() {
    return List.of(this.textField);
  }
}