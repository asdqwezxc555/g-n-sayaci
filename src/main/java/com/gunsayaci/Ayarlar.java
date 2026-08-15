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
 * hedefGun: kullanıcının girdiği sayı.
 * baslangicGun: kaydedildiği andaki oyun içi mutlak gün sayısı; hesaplama bu güne göre yapılır.
 * gostergeGoster: gösterge ekranda görünsün mü?
 * geriSayimMi: true ise hedefGun'dan aşağı sayar, false ise hedefGun'dan yukarı sayar.
 */
public class Ayarlar {
	public static int hedefGun = 100;
	public static long baslangicGun = 0;
	public static boolean gostergeGoster = true;
	public static boolean geriSayimMi = true;

	private static Path dosyaYolu() {
		return FabricLoader.getInstance().getConfigDir().resolve("gunsayaci.txt");
	}

	public static void yukle() {
		try {
			Path yol = dosyaYolu();
			if (Files.exists(yol)) {
				List<String> satirlar = Files.readAllLines(yol, StandardCharsets.UTF_8);
				if (satirlar.size() > 0) hedefGun = Integer.parseInt(satirlar.get(0).trim());
				if (satirlar.size() > 1) baslangicGun = Long.parseLong(satirlar.get(1).trim());
				if (satirlar.size() > 2) gostergeGoster = satirlar.get(2).trim().equals("1");
				if (satirlar.size() > 3) geriSayimMi = satirlar.get(3).trim().equals("1");
			}
		} catch (Exception e) {
			hedefGun = 100;
			baslangicGun = 0;
			gostergeGoster = true;
			geriSayimMi = true;
		}
	}

	private static void diskeYaz() {
		try {
			String icerik = hedefGun + "\n" + baslangicGun + "\n"
					+ (gostergeGoster ? "1" : "0") + "\n"
					+ (geriSayimMi ? "1" : "0");
			Files.writeString(dosyaYolu(), icerik, StandardCharsets.UTF_8);
		} catch (IOException ignored) {
			// Kaydedilemezse bile mevcut oturumda bellekteki deger kullanilmaya devam eder.
		}
	}

	public static void kaydet(int yeniHedef, long mevcutMutlakGun, boolean yeniGeriSayimMi) {
		hedefGun = yeniHedef;
		baslangicGun = mevcutMutlakGun;
		geriSayimMi = yeniGeriSayimMi;
		diskeYaz();
	}

	public static void gostergeyiDegistir() {
		gostergeGoster = !gostergeGoster;
		diskeYaz();
	}
}
