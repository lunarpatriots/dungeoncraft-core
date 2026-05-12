package com.lunarpatriots.dungeoncraft.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class PasswordTextFieldWidget extends TextFieldWidget {

  public PasswordTextFieldWidget(
    TextRenderer textRenderer,
    int x,
    int y,
    int width,
    int height,
    Text placeholder
  ) {
    super(textRenderer, x, y, width, height, placeholder);
  }

  @Override
  public void renderWidget(
    DrawContext context,
    int mouseX,
    int mouseY,
    float delta
  ) {
    // Save real text
    String realText = this.getText();

    super.setText("•".repeat(realText.length()));

    super.renderWidget(context, mouseX, mouseY, delta);

    super.setText(realText);
  }
}