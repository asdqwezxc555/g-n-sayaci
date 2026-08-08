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

		int genislik = 78;
		int yukseklik = 28;
		int x = client.getWindow().getScaledWidth() - genislik - 6;
		int y = 6;

		cerceveCiz(context, x, y, genislik, yukseklik);

		String gunMetni = "G:" + mevcutGun + "/" + hedefGun;
		String kalanMetni = kalanTick > 0 ? zamanFormatla(kalanTick) : "Bitti!";

		context.drawTextWithShadow(client.textRenderer, gunMetni, x + 5, y + 6, 0xFFF5DEB3);
		context.drawTextWithShadow(client.textRenderer, kalanMetni, x + 5, y + 16, 0xFFF5DEB3);
	}

	/** Vanilla dokusuna ihtiyaç duymadan, tamamen kod ile ahşap görünümlü bir çerçeve çizer. */
	private void cerceveCiz(DrawContext context, int x, int y, int genislik, int yukseklik) {
		context.fill(x, y, x + genislik, y + yukseklik, 0xFF3B2412);
		context.fill(x + 2, y + 2, x + genislik - 2, y + yukseklik - 2, 0xFF8B5A2B);

		for (int cizgiY = y + 2; cizgiY < y + yukseklik - 2; cizgiY += 5) {
			context.fill(x + 2, cizgiY, x + genislik - 2, cizgiY + 1, 0x33000000);
		}

		context.fill(x + 2, y + 2, x + genislik - 2, y + 3, 0x40FFFFFF);
		context.fill(x + 3, y + 3, x + genislik - 3, y + yukseklik - 3, 0x80000000);

		int[][] kosler = {
				{x + 2, y + 2}, {x + genislik - 4, y + 2},
				{x + 2, y + yukseklik - 4}, {x + genislik - 4, y + yukseklik - 4}
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
