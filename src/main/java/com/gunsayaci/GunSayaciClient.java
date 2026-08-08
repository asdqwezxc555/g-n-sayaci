package com.gunsayaci;

import com.gunsayaci.gui.AyarEkrani;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GunSayaciClient implements ClientModInitializer {
	private static KeyBinding acmaTusu;

	@Override
	public void onInitializeClient() {
		Ayarlar.yukle();

		acmaTusu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.gunsayaci.ac",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				"key.categories.gunsayaci"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (acmaTusu.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(new AyarEkrani(Ayarlar.hedefGun));
				}
			}
		});

		HudRenderCallback.EVENT.register(this::ciz);
	}

	private void ciz(DrawContext context, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null || client.options.hudHidden) return;

		long zamanToplam = client.world.getTimeOfDay();
		long mevcutGun = zamanToplam / 24000L;
		int hedefGun = Ayarlar.hedefGun;
		long kalanTick = (long) hedefGun * 24000L - zamanToplam;

		int genislik = 150;
		int yukseklik = 56;
		int x = client.getWindow().getScaledWidth() - genislik - 8;
		int y = 8;

		cerceveCiz(context, x, y, genislik, yukseklik);

		String gunMetni = "Gün: " + mevcutGun + " / " + hedefGun;
		String kalanMetni = kalanTick > 0 ? "Kalan: " + zamanFormatla(kalanTick) : "Hedef güne ulaşıldı!";

		context.drawTextWithShadow(client.textRenderer, gunMetni, x + 10, y + 14, 0xFFF5DEB3);
		context.drawTextWithShadow(client.textRenderer, kalanMetni, x + 10, y + 32, 0xFFF5DEB3);
	}

	/** Vanilla dokusuna ihtiyaç duymadan, tamamen kod ile ahşap görünümlü bir çerçeve çizer. */
	private void cerceveCiz(DrawContext context, int x, int y, int genislik, int yukseklik) {
		context.fill(x, y, x + genislik, y + yukseklik, 0xFF3B2412);
		context.fill(x + 3, y + 3, x + genislik - 3, y + yukseklik - 3, 0xFF8B5A2B);

		for (int cizgiY = y + 3; cizgiY < y + yukseklik - 3; cizgiY += 6) {
			context.fill(x + 3, cizgiY, x + genislik - 3, cizgiY + 1, 0x33000000);
		}

		context.fill(x + 3, y + 3, x + genislik - 3, y + 5, 0x40FFFFFF);
		context.fill(x + 6, y + 6, x + genislik - 6, y + yukseklik - 6, 0x80000000);

		int[][] kosler = {
				{x + 5, y + 5}, {x + genislik - 7, y + 5},
				{x + 5, y + yukseklik - 7}, {x + genislik - 7, y + yukseklik - 7}
		};
		for (int[] k : kosler) {
			context.fill(k[0], k[1], k[0] + 2, k[1] + 2, 0xFF2A1808);
		}
	}

	private String zamanFormatla(long ticks) {
		long saniyeToplam = ticks / 20L;
		long saat = saniyeToplam / 3600L;
		long dakika = (saniyeToplam % 3600L) / 60L;
		long saniye = saniyeToplam % 60L;
		if (saat > 0) {
			return String.format("%d:%02d:%02d", saat, dakika, saniye);
		}
		return String.format("%d:%02d", dakika, saniye);
	}
}
