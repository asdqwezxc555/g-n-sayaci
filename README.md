# Gün Sayacı (Fabric 1.20.1)

Ekranın sağ üst köşesinde ahşap görünümlü küçük bir çerçeve içinde şunu
gösterir: **kaçıncı günde olduğun** ve **belirlediğin bitiş gününe ne
kadar süre kaldığı**.

## Neden bu mod çok daha sağlam olmalı

Bu mod tamamen **istemci tarafında** çalışır:
- Blok yok, komut yok, datapack yok, ağ paketi yok, sunucu bağımlılığı yok.
- Sadece ekrana çizim yapar ve küçük bir ayar dosyasına (yerel diskine)
  hedef günü kaydeder.
- Ahşap çerçeve, harici bir resim dosyasına ihtiyaç duymadan doğrudan
  kod içinde renkli dikdörtgenlerle çizilir — bu yüzden eksik dosya
  riski yok.

Bu yüzden önceki modlardaki "hile izni", "reload", "bilinmeyen ..." gibi
sorunların hiçbiri burada mümkün değil.

## Derleme (GitHub Actions ile)

1. Bu klasörü (`gunsayaci`) yeni bir GitHub deposuna yükle (`.github`
   klasörü dahil — gizli dosya uyarısı çıkarsa "Add file > Create new
   file" ile `.github/workflows/build.yml` yolunu yazıp içeriğini elle
   yapıştır).
2. "Actions" sekmesinde "Modu Derle" işleminin yeşil tik almasını bekle.
3. Çalışmanın altındaki "Artifacts" bölümünden `gunsayaci-jar` dosyasını
   indir, içinden `gunsayaci-1.0.0.jar` çıkacak.

## Kurulum

1. [Fabric Loader](https://fabricmc.net/use/) ile 1.20.1'i kur.
2. [Fabric API](https://modrinth.com/mod/fabric-api) modunu (1.20.1) indirip
   `.minecraft/mods` klasörüne at.
3. `gunsayaci-1.0.0.jar` dosyasını da aynı klasöre at.
4. Fabric profiliyle oyunu başlat.

## Kullanım

1. Oyuna girdiğinde sağ üst köşede çerçeveyi otomatik göreceksin
   (varsayılan hedef gün: 100).
2. Hedef günü değiştirmek için **O** tuşuna bas (istersen Seçenekler >
   Kontroller > "Gün Sayacı" kategorisinden başka bir tuşa da atayabilirsin).
3. Açılan ekranda yeni hedef günü yaz, **Kaydet**'e bas, **Kapat**'a bas.
4. Gösterge anında güncellenir; ayar bir dosyada saklandığı için oyunu
   kapatıp açsan da hatırlanır.
