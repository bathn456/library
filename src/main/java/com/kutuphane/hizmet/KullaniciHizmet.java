package com.kutuphane.hizmet;

import com.kutuphane.depo.KullaniciDepo;
import com.kutuphane.depo.KullaniciRolDepo;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.KullaniciRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KullaniciHizmet {

    private final KullaniciDepo kullaniciDepo;
    private final KullaniciRolDepo kullaniciRolDepo;

    @Autowired
    public KullaniciHizmet(KullaniciDepo kullaniciDepo, KullaniciRolDepo kullaniciRolDepo) {
        this.kullaniciDepo = kullaniciDepo;
        this.kullaniciRolDepo = kullaniciRolDepo;
    }

    public List<Kullanici> tumKullanicilariGetir() {
        return kullaniciDepo.findAll();
    }

    public Optional<Kullanici> kullaniciGetir(Integer no) {
        return kullaniciDepo.findById(no);
    }

    public List<Kullanici> kullaniciAra(String arama) {
        return kullaniciDepo.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(arama, arama);
    }

    public List<Kullanici> rolIleGetir(String rol) {
        return kullaniciDepo.findByRol(rol);
    }

    @Transactional
    public Kullanici kullaniciEkle(Kullanici kullanici) {
        if (kullanici.getUyelikTarihi() == null) {
            kullanici.setUyelikTarihi(LocalDateTime.now());
        }
        
        if (kullanici.getDurum() == null) {
            kullanici.setDurum("AKTIF");
        }
        
        KullaniciRol yoneticiRol = kullaniciRolDepo.findByRol("YONETICI");
        if (yoneticiRol == null) {
            yoneticiRol = new KullaniciRol();
            yoneticiRol.setRol("YONETICI");
            yoneticiRol = kullaniciRolDepo.save(yoneticiRol);
        }
        
        if (kullanici.getKullaniciRol() == null || kullanici.getKullaniciRol().getNo() == null) {
            kullanici.setKullaniciRol(yoneticiRol);
        } else {
            kullaniciRolDepo.findById(kullanici.getKullaniciRol().getNo())
                .ifPresent(kullanici::setKullaniciRol);
        }
        
        return kullaniciDepo.save(kullanici);
    }

    @Transactional
    public Kullanici kullaniciGuncelle(Integer no, Kullanici kullaniciDetaylari) {
        return kullaniciDepo.findById(no)
                .map(kullanici -> {
                    kullanici.setAd(kullaniciDetaylari.getAd());
                    kullanici.setSoyad(kullaniciDetaylari.getSoyad());
                    kullanici.setTelefonNumarasi(kullaniciDetaylari.getTelefonNumarasi());
                    kullanici.setEposta(kullaniciDetaylari.getEposta());
                    kullanici.setAdres(kullaniciDetaylari.getAdres());
                    
                    if (kullaniciDetaylari.getDurum() != null) {
                        kullanici.setDurum(kullaniciDetaylari.getDurum());
                    }
                    
                    if (kullaniciDetaylari.getKullaniciRol() != null && kullaniciDetaylari.getKullaniciRol().getNo() != null) {
                        kullaniciRolDepo.findById(kullaniciDetaylari.getKullaniciRol().getNo())
                            .ifPresent(kullanici::setKullaniciRol);
                    }
                    
                    return kullaniciDepo.save(kullanici);
                })
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + no));
    }

    @Transactional
    public void kullaniciSil(Integer no) {
        kullaniciDepo.deleteById(no);
    }

    @Transactional
    public void durumGuncelle(Integer no, String durum) {
        kullaniciDepo.findById(no)
                .ifPresent(kullanici -> {
                    kullanici.setDurum(durum);
                    kullaniciDepo.save(kullanici);
                });
    }
}
