
-- Kullanıcı Rolleri
INSERT INTO kullanici_rol (rol) VALUES ('YONETICI');

-- Yönetici Kullanıcılar
INSERT INTO kullanici (ad, soyad, eposta, telefon_numarasi, adres, uyelik_tarihi, durum, rol_no) 
VALUES ('Admin', 'User', 'admin@kutuphane.com', '5551234567', 'Ankara', CURRENT_TIMESTAMP, 'AKTIF', 1);

-- Odalar
INSERT INTO oda (isim, aciklama, kat) VALUES
('Ana Salon', 'Ana okuma salonu', 1),
('Referans', 'Referans kitapları bölümü', 1);

-- Kitaplıklar
INSERT INTO kitaplik (isim, aciklama, oda_no) VALUES
('A Kitaplığı', 'Roman ve Hikayeler', 1),
('B Kitaplığı', 'Bilim ve Teknoloji', 1);

-- Raflar
INSERT INTO raf (isim, aciklama, kapasite, kitaplik_no) VALUES
('A1', 'Yerli Romanlar', 100, 1),
('A2', 'Yabancı Romanlar', 100, 1);

-- Kategoriler
INSERT INTO kategori (isim, aciklama) VALUES
('Roman', 'Roman kitapları'),
('Bilim', 'Bilim kitapları');

-- Kitaplar
INSERT INTO kitap (baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no, raf_no) VALUES
('Bülbülü Öldürmek', 'Harper Lee', '9789750719387', '1960-07-11', 5, 3, 1, 2),
('1984', 'George Orwell', '9789750718069', '1949-06-08', 3, 2, 1, 2),
('Suç ve Ceza', 'Fyodor Dostoyevski', '9789750718762', '1866-01-01', 4, 4, 1, 2),
('İnce Memed', 'Yaşar Kemal', '9789750738609', '1955-01-01', 3, 3, 1, 1);
