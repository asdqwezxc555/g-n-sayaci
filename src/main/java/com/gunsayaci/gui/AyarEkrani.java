package com.gunsayaci.gui;

import com.gunsayaci.Ayarlar;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AyarEkrani extends Screen {
	private final int mevcutHedef;
	private TextFieldWidget alan;
	private Text durum = Text.empty();

	public AyarEkrani(int mevcutHedef) {
		super(Text.literal("Gün Sayacı Ayarları"));
		this.mevcutHedef = mevcutHedef;
	}

	@Override
	protected void init() {
		int ortaX = this.width / 2;

		alan = new TextFieldWidget(this.textRenderer, ortaX - 60, 70, 120, 20, Text.literal("Hedef Gün"));
		alan.setMaxLength(6);
		alan.setText(String.valueOf(mevcutHedef));
		this.addDrawableChild(alan);
		this.setInitialFocus(alan);

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Kaydet"), b -> kaydet())
				.dimensions(ortaX - 60, 100, 120, 20).build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> this.close())
				.dimensions(ortaX - 60, 125, 120, 20).build());
	}

	private void kaydet() {
		try {
			int deger = Integer.parseInt(alan.getText().trim());
			if (deger < 0) deger = 0;
			Ayarlar.kaydet(deger);
			durum = Text.literal("§aKaydedildi!");
		} catch (NumberFormatException e) {
			durum = Text.literal("§cLütfen geçerli bir tam sayı gir.");
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 30, 0xFFFFFF);
		context.drawCenteredTextWithShadow(this.textRenderer,
				Text.literal("Sayaç kaçıncı günde bitsin?"), this.width / 2, 55, 0xAAAAAA);
		context.drawCenteredTextWithShadow(this.textRenderer, durum, this.width / 2, 150, 0xFFFFFF);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
