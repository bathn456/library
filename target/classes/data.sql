-- Roller (Sadece Yönetici)
INSERT INTO kullanici_rol (no, rol) VALUES (1, 'YONETICI');

-- Yönetici Kullanıcılar
INSERT INTO kullanici (no, ad, soyad, telefon_numarasi, eposta, uyelik_tarihi, durum, adres, rol_no) 
VALUES (1, 'Ahmet', 'Yılmaz', '5551234567', 'ahmet@kutuphane.com', CURRENT_TIMESTAMP(), 'AKTIF', 'Ankara, Çankaya', 1);

-- Odalar
INSERT INTO oda (no, isim, kat, aciklama) 
VALUES (1, 'Ana Salon', 1, 'Ana okuma salonu');

-- Kitaplıklar
INSERT INTO kitaplik (no, isim, aciklama, oda_no) 
VALUES (1, 'A Kitaplığı', 'Edebiyat Bölümü', 1);

-- Raflar
INSERT INTO raf (no, isim, kapasite, aciklama, kitaplik_no) 
VALUES (1, 'A1', 50, 'Roman Rafı', 1);

-- Kategoriler
INSERT INTO kategori (no, isim, aciklama, ust_kategori_no) 
VALUES (1, 'Dünya Klasikleri', 'Klasik Eserler', NULL);

-- Kitaplar
INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no, raf_no) 
VALUES 
  (1, 'Suç ve Ceza', 'Fyodor Dostoyevski', '9789750726514', PARSEDATETIME('1866-01-01', 'yyyy-MM-dd'), 3, 3, 1, 1),
  (2, 'Bülbülü Öldürmek', 'Harper Lee', '9789750726439', PARSEDATETIME('1960-07-11', 'yyyy-MM-dd'), 2, 2, 1, 1);