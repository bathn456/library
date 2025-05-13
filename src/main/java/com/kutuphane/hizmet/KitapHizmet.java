package com.kutuphane.hizmet;

import com.kutuphane.depo.KitapDepo;
import com.kutuphane.depo.KategoriDepo;
import com.kutuphane.varlik.Kitap;
import com.kutuphane.varlik.Kategori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KitapHizmet {

    private final KitapDepo kitapDepo;
    private final KategoriDepo kategoriDepo;

    @Autowired
    public KitapHizmet(KitapDepo kitapDepo, KategoriDepo kategoriDepo) {
        this.kitapDepo = kitapDepo;
        this.kategoriDepo = kategoriDepo;
    }

    public List<Kitap> tumKitaplariGetir() {
        return kitapDepo.findAll();
    }

    public List<Kitap> mevutKitaplariGetir() {
        return kitapDepo.findAllAvailableBooks();
    }

    public Optional<Kitap> kitapGetir(Integer no) {
        return kitapDepo.findById(no);
    }

    public List<Kitap> kitapAra(String arama) {
        return kitapDepo.findByBaslikContainingIgnoreCase(arama);
    }

    public List<Kitap> yazarAra(String yazar) {
        return kitapDepo.findByYazarContainingIgnoreCase(yazar);
    }

    public List<Kitap> kategoriIleGetir(Integer kategoriNo) {
        return kitapDepo.findByKategoriNo(kategoriNo);
    }

    @Transactional
    public Kitap kitapEkle(Kitap kitap) {
        if (kitap.getMevcutAdet() == null) {
            kitap.setMevcutAdet(kitap.getToplamAdet());
        }
        
        if (kitap.getKategori() != null && kitap.getKategori().getNo() != null) {
            Optional<Kategori> kategori = kategoriDepo.findById(kitap.getKategori().getNo());
            kategori.ifPresent(kitap::setKategori);
        }
        
        return kitapDepo.save(kitap);
    }

    @Transactional
    public Kitap kitapGuncelle(Integer no, Kitap kitapDetaylari) {
        return kitapDepo.findById(no)
                .map(kitap -> {
                    kitap.setBaslik(kitapDetaylari.getBaslik());
                    kitap.setYazar(kitapDetaylari.getYazar());
                    kitap.setIsbn(kitapDetaylari.getIsbn());
                    kitap.setYayinYili(kitapDetaylari.getYayinYili());
                    
                    if (kitapDetaylari.getKategori() != null && kitapDetaylari.getKategori().getNo() != null) {
                        Optional<Kategori> kategori = kategoriDepo.findById(kitapDetaylari.getKategori().getNo());
                        kategori.ifPresent(kitap::setKategori);
                    }
                    
                    if (kitapDetaylari.getToplamAdet() != null) {
                        int eskiToplam = kitap.getToplamAdet();
                        int yeniToplam = kitapDetaylari.getToplamAdet();
                        int fark = yeniToplam - eskiToplam;
                        
                        kitap.setToplamAdet(yeniToplam);
                        kitap.setMevcutAdet(kitap.getMevcutAdet() + fark);
                    }
                    
                    return kitapDepo.save(kitap);
                })
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + no));
    }

    @Transactional
    public void kitapSil(Integer no) {
        kitapDepo.deleteById(no);
    }

    @Transactional
    public boolean stokGuncelle(Integer kitapNo, int miktar, boolean arttir) {
        return kitapDepo.findById(kitapNo)
                .map(kitap -> {
                    int yeniMiktar = arttir 
                            ? kitap.getMevcutAdet() + miktar 
                            : kitap.getMevcutAdet() - miktar;
                    
                    if (yeniMiktar < 0 || yeniMiktar > kitap.getToplamAdet()) {
                        return false;
                    }
                    
                    kitap.setMevcutAdet(yeniMiktar);
                    kitapDepo.save(kitap);
                    return true;
                })
                .orElse(false);
    }
}
