package com.kutuphane.hizmet;

import com.kutuphane.depo.KategoriDepo;
import com.kutuphane.varlik.Kategori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KategoriHizmet {

    private final KategoriDepo kategoriDepo;

    @Autowired
    public KategoriHizmet(KategoriDepo kategoriDepo) {
        this.kategoriDepo = kategoriDepo;
    }

    public List<Kategori> tumKategorileriGetir() {
        return kategoriDepo.findAll();
    }

    public Optional<Kategori> kategoriGetir(Integer no) {
        return kategoriDepo.findById(no);
    }

    public List<Kategori> kategoriAra(String isim) {
        return kategoriDepo.findByIsimContainingIgnoreCase(isim);
    }

    public List<Kategori> ustKategorileriGetir() {
        return kategoriDepo.findByUstKategoriNoIsNull();
    }

    public List<Kategori> altKategorileriGetir(Integer ustKategoriNo) {
        return kategoriDepo.findByUstKategoriNo(ustKategoriNo);
    }

    @Transactional
    public Kategori kategoriEkle(Kategori kategori) {
        return kategoriDepo.save(kategori);
    }

    @Transactional
    public Kategori kategoriGuncelle(Integer no, Kategori kategoriDetaylari) {
        return kategoriDepo.findById(no)
                .map(kategori -> {
                    kategori.setIsim(kategoriDetaylari.getIsim());
                    kategori.setAciklama(kategoriDetaylari.getAciklama());
                    kategori.setUstKategoriNo(kategoriDetaylari.getUstKategoriNo());
                    return kategoriDepo.save(kategori);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + no));
    }

    @Transactional
    public void kategoriSil(Integer no) {
        kategoriDepo.deleteById(no);
    }
}
