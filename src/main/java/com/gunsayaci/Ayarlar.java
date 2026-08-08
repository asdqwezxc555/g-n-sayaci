package com.gunsayaci;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Sayaç ayarlarını basit bir metin dosyasında saklar (.minecraft/config/gunsayaci.txt).
 * Sunucuya, dünya kaydına veya ağa ihtiyaç duymaz; tamamen istemci tarafında çalışır.
 *
 * hedefGun: kullanıcının girdiği ve sayacın başlayacağı sayı (ör. 100).
 * baslangicGun: kaydedildiği andaki oyun içi mutlak gün sayısı; geri sayım
 * bu güne göre hesaplanır (kalanGun = hedefGun - (mevcutGun - baslangicGun)).
 */
public class Ayarlar {
	public static int hedefGun = 100;
	public static long baslangicGun = 0;

	private static Path dosyaYolu() {
		return FabricLoader.getInstance().getConfigDir().resolve("gunsayaci.txt");
	}

	public static void yukle() {
		try {
			Path yol = dosyaYolu();
			if (Files.exists(yol)) {
				List<String> satirlar = Files.readAllLines(yol, StandardCharsets.UTF_8);
				if (!satirlar.isEmpty()) hedefGun = Integer.parseInt(satirlar.get(0).trim());
				if (satirlar.size() > 1) baslangicGun = Long.parseLong(satirlar.get(1).trim());
			}
		} catch (Exception e) {
			hedefGun = 100;
			baslangicGun = 0;
		}
	}

	public static void kaydet(int yeniHedef, long mevcutMutlakGun) {
		hedefGun = yeniHedef;
		baslangicGun = mevcutMutlakGun;
		try {
			Files.writeString(dosyaYolu(), yeniHedef + "\n" + mevcutMutlakGun, StandardCharsets.UTF_8);
		} catch (IOException ignored) {
			// Kaydedilemezse bile mevcut oturumda bellekteki deger kullanilmaya devam eder.
		}
	}
}
