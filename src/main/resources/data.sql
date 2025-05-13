-- Roller
INSERT INTO kullanici_rol (no, rol) VALUES (1, 'YONETICI');
INSERT INTO kullanici_rol (no, rol) VALUES (2, 'OGRENCI');

-- Kullanicilar
INSERT INTO kullanici (no, ad, soyad, telefon_numarasi, eposta, uyelik_tarihi, durum, adres, rol_no) 
VALUES (1, 'Ahmet', 'Yılmaz', '5551234567', 'ahmet.yilmaz@example.com', CURRENT_TIMESTAMP(), 'AKTIF', 'Ankara, Çankaya', 1);

INSERT INTO kullanici (no, ad, soyad, telefon_numarasi, eposta, uyelik_tarihi, durum, adres, rol_no) 
VALUES (2, 'Ayşe', 'Demir', '5559876543', 'ayse.demir@example.com', CURRENT_TIMESTAMP(), 'AKTIF', 'İstanbul, Kadıköy', 1);

INSERT INTO kullanici (no, ad, soyad, telefon_numarasi, eposta, uyelik_tarihi, durum, adres, rol_no) 
VALUES (3, 'Mehmet', 'Kaya', '5552223333', 'mehmet.kaya@example.com', CURRENT_TIMESTAMP(), 'AKTIF', 'İzmir, Bornova', 2);

INSERT INTO kullanici (no, ad, soyad, telefon_numarasi, eposta, uyelik_tarihi, durum, adres, rol_no) 
VALUES (4, 'Zeynep', 'Çelik', '5554445555', 'zeynep.celik@example.com', CURRENT_TIMESTAMP(), 'AKTIF', 'Bursa, Nilüfer', 2);

-- Kategoriler
INSERT INTO kategori (no, isim, aciklama, ust_kategori_no) 
VALUES (1, 'Roman', 'Roman türündeki kitaplar', NULL);

INSERT INTO kategori (no, isim, aciklama, ust_kategori_no) 
VALUES (2, 'Bilim', 'Bilimsel kitaplar', NULL);

INSERT INTO kategori (no, isim, aciklama, ust_kategori_no) 
VALUES (3, 'Klasik Romanlar', 'Klasik edebiyat eserleri', 1);

INSERT INTO kategori (no, isim, aciklama, ust_kategori_no) 
VALUES (4, 'Fizik', 'Fizik alanında kitaplar', 2);

-- Kitaplar
INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no) 
VALUES (1, 'Bülbülü Öldürmek', 'Harper Lee', '9789750726439', PARSEDATETIME('1960-07-11', 'yyyy-MM-dd'), 5, 4, 3);

INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no) 
VALUES (2, 'Suç ve Ceza', 'Fyodor Dostoyevski', '9789750726514', PARSEDATETIME('1866-01-01', 'yyyy-MM-dd'), 3, 3, 3);

INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no) 
VALUES (3, 'Simyacı', 'Paulo Coelho', '9789750726712', PARSEDATETIME('1988-01-01', 'yyyy-MM-dd'), 7, 6, 1);

INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no) 
VALUES (4, 'Fiziğin Tematik Tarihi', 'Richard Feynman', '9789750726231', PARSEDATETIME('1985-01-01', 'yyyy-MM-dd'), 2, 2, 4);

INSERT INTO kitap (no, baslik, yazar, isbn, yayin_yili, toplam_adet, mevcut_adet, kategori_no) 
VALUES (5, 'Evrenin Kısa Tarihi', 'Stephen Hawking', '9789750726111', PARSEDATETIME('1988-01-01', 'yyyy-MM-dd'), 4, 4, 4);

-- Ödünç alma işlemleri
INSERT INTO odunc_alma (no, odunc_alma_tarihi, son_teslim_tarihi, durum, toplam_odunc, kullanici_no) 
VALUES (1, DATEADD('DAY', -10, CURRENT_TIMESTAMP()), DATEADD('DAY', 4, CURRENT_TIMESTAMP()), 'AKTIF', 1, 3);

INSERT INTO odunc_alma_ogesi (no, odunc_alma_no, kitap_no, adet, durum) 
VALUES (1, 1, 1, 1, 'TESLIM_EDILMEDI');

-- Kitap stok güncellemesi (Ödünç alman kitap sayısını güncelle)
UPDATE kitap SET mevcut_adet = mevcut_adet - 1 WHERE no = 1;

-- Rezervasyon işlemleri
INSERT INTO rezervasyon (no, kullanici_no, rezervasyon_tarihi, durum) 
VALUES (1, 4, CURRENT_TIMESTAMP(), 'BEKLIYOR');

INSERT INTO rezervasyon_ogesi (no, rezervasyon_no, kitap_no, adet, durum) 
VALUES (1, 1, 3, 1, 'BEKLIYOR');
