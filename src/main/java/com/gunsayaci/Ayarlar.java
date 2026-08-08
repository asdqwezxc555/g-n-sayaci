package com.gunsayaci;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Hedef gün ayarını basit bir metin dosyasında saklar (.minecraft/config/gunsayaci.txt).
 * Sunucuya, dünya kaydına veya ağa ihtiyaç duymaz; tamamen istemci tarafında çalışır.
 */
public class Ayarlar {
	public static int hedefGun = 100;

	private static Path dosyaYolu() {
		return FabricLoader.getInstance().getConfigDir().resolve("gunsayaci.txt");
	}

	public static void yukle() {
		try {
			Path yol = dosyaYolu();
			if (Files.exists(yol)) {
				String icerik = Files.readString(yol, StandardCharsets.UTF_8).trim();
				hedefGun = Integer.parseInt(icerik);
			}
		} catch (Exception e) {
			hedefGun = 100;
		}
	}

	public static void kaydet(int yeniHedef) {
		hedefGun = yeniHedef;
		try {
			Files.writeString(dosyaYolu(), String.valueOf(yeniHedef), StandardCharsets.UTF_8);
		} catch (IOException ignored) {
			// Kaydedilemezse bile mevcut oturumda bellekteki deger kullanilmaya devam eder.
		}
	}
}
