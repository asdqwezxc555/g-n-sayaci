package com.gunsayaci.gui;

import com.gunsayaci.Ayarlar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AyarEkrani extends Screen {
	private final int mevcutHedef;
	private TextFieldWidget alan;
	private Text durum = Text.empty();
	private boolean seciliYonGeri = Ayarlar.geriSayimMi;

	public AyarEkrani(int mevcutHedef) {
		super(Text.literal("Gün Sayacı Ayarları"));
		this.mevcutHedef = mevcutHedef;
	}

	@Override
	protected void init() {
		int ortaX = this.width / 2;

		alan = new TextFieldWidget(this.textRenderer, ortaX - 60, 55, 120, 20, Text.literal("Hedef Gün"));
		alan.setMaxLength(6);
		alan.setText(String.valueOf(mevcutHedef));
		this.addDrawableChild(alan);
		this.setInitialFocus(alan);

		this.addDrawableChild(CyclingButtonWidget.<Boolean>builder(deger -> Text.literal(deger ? "Yön: Geri Sayım (azalır)" : "Yön: İleri Sayım (artar)"))
				.values(true, false)
				.initially(seciliYonGeri)
				.build(ortaX - 90, 82, 180, 20, Text.literal("Yön"),
						(btn, deger) -> seciliYonGeri = deger));

		this.addDrawableChild(CyclingButtonWidget.<Boolean>builder(deger -> Text.literal(deger ? "Gösterge: Açık" : "Gösterge: Kapalı"))
				.values(true, false)
				.initially(Ayarlar.gostergeGoster)
				.build(ortaX - 90, 109, 180, 20, Text.literal("Gösterge"),
						(btn, deger) -> {
							if (deger != Ayarlar.gostergeGoster) {
								Ayarlar.gostergeyiDegistir();
							}
						}));

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Kaydet"), b -> kaydet())
				.dimensions(ortaX - 90, 140, 180, 20).build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> this.close())
				.dimensions(ortaX - 90, 165, 180, 20).build());
	}

	private void kaydet() {
		try {
			int deger = Integer.parseInt(alan.getText().trim());
			if (deger < 0) deger = 0;

			long mevcutMutlakGun = 0;
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.world != null) {
				mevcutMutlakGun = client.world.getTimeOfDay() / 24000L;
			}

			Ayarlar.kaydet(deger, mevcutMutlakGun, seciliYonGeri);
			durum = Text.literal("§aKaydedildi!");
		} catch (NumberFormatException e) {
			durum = Text.literal("§cLütfen geçerli bir tam sayı gir.");
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
		context.drawCenteredTextWithShadow(this.textRenderer,
				Text.literal("Hedef gün sayısı"), this.width / 2, 42, 0xAAAAAA);
		context.drawCenteredTextWithShadow(this.textRenderer, durum, this.width / 2, 195, 0xFFFFFF);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
