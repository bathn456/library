package com.kutuphane.hizmet;

import com.kutuphane.depo.KitapDepo;
import com.kutuphane.depo.KullaniciDepo;
import com.kutuphane.depo.OduncAlmaDepo;
import com.kutuphane.depo.OduncAlmaOgesiDepo;
import com.kutuphane.varlik.Kitap;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.OduncAlma;
import com.kutuphane.varlik.OduncAlmaOgesi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OduncAlmaHizmet {

    private final OduncAlmaDepo oduncAlmaDepo;
    private final OduncAlmaOgesiDepo oduncAlmaOgesiDepo;
    private final KitapDepo kitapDepo;
    private final KullaniciDepo kullaniciDepo;
    private final static int VARSAYILAN_ODUNC_GUN = 14;

    @Autowired
    public OduncAlmaHizmet(OduncAlmaDepo oduncAlmaDepo, OduncAlmaOgesiDepo oduncAlmaOgesiDepo,
                          KitapDepo kitapDepo, KullaniciDepo kullaniciDepo) {
        this.oduncAlmaDepo = oduncAlmaDepo;
        this.oduncAlmaOgesiDepo = oduncAlmaOgesiDepo;
        this.kitapDepo = kitapDepo;
        this.kullaniciDepo = kullaniciDepo;
    }

    public List<OduncAlma> tumOduncAlmalariGetir() {
        return oduncAlmaDepo.findAll();
    }

    public Optional<OduncAlma> oduncAlmaGetir(Integer no) {
        return oduncAlmaDepo.findById(no);
    }

    public List<OduncAlma> kullaniciOduncAlmalariGetir(Integer kullaniciNo) {
        return oduncAlmaDepo.findByKullaniciNo(kullaniciNo);
    }

    public List<OduncAlma> durumIleGetir(String durum) {
        return oduncAlmaDepo.findByDurum(durum);
    }

    public List<OduncAlma> süresiGecmisOduncAlmalariGetir() {
        return oduncAlmaDepo.findOverdueLoans(LocalDateTime.now());
    }

    @Transactional
    public OduncAlma oduncAlmaOlustur(Integer kullaniciNo, List<Integer> kitapNoLari, List<Integer> adetler) {
        if (kitapNoLari.size() != adetler.size()) {
            throw new IllegalArgumentException("Kitap ve adet listeleri aynı boyutta olmalıdır.");
        }
        
        Kullanici kullanici = kullaniciDepo.findById(kullaniciNo)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + kullaniciNo));
        
        OduncAlma oduncAlma = new OduncAlma();
        oduncAlma.setKullanici(kullanici);
        oduncAlma.setOduncAlmaTarihi(LocalDateTime.now());
        oduncAlma.setSonTeslimTarihi(LocalDateTime.now().plusDays(VARSAYILAN_ODUNC_GUN));
        oduncAlma.setDurum("AKTIF");
        oduncAlma.setToplamOdunc(kitapNoLari.size());
        
        OduncAlma kaydedilenOduncAlma = oduncAlmaDepo.save(oduncAlma);
        
        for (int i = 0; i < kitapNoLari.size(); i++) {
            Integer kitapNo = kitapNoLari.get(i);
            Integer adet = adetler.get(i);
            
            Kitap kitap = kitapDepo.findById(kitapNo)
                    .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + kitapNo));
            
            if (kitap.getMevcutAdet() < adet) {
                throw new RuntimeException("Yeterli miktarda kitap yok. Mevcut: " + kitap.getMevcutAdet() + ", İstenen: " + adet);
            }
            
            OduncAlmaOgesi oduncAlmaOgesi = new OduncAlmaOgesi();
            oduncAlmaOgesi.setOduncAlma(kaydedilenOduncAlma);
            oduncAlmaOgesi.setKitap(kitap);
            oduncAlmaOgesi.setAdet(adet);
            oduncAlmaOgesi.setDurum("TESLIM_EDILMEDI");
            
            oduncAlmaOgesiDepo.save(oduncAlmaOgesi);
            
            kitap.setMevcutAdet(kitap.getMevcutAdet() - adet);
            kitapDepo.save(kitap);
        }
        
        return kaydedilenOduncAlma;
    }

    @Transactional
    public boolean oduncAlmaIadeEt(Integer oduncAlmaNo) {
        return oduncAlmaDepo.findById(oduncAlmaNo)
                .map(oduncAlma -> {
                    oduncAlma.setDurum("TAMAMLANDI");
                    
                    List<OduncAlmaOgesi> ogeler = oduncAlmaOgesiDepo.findByOduncAlmaNo(oduncAlmaNo);
                    for (OduncAlmaOgesi oge : ogeler) {
                        oge.setIadeTarihi(LocalDateTime.now());
                        oge.setDurum("TESLIM_EDILDI");
                        oduncAlmaOgesiDepo.save(oge);
                        
                        Kitap kitap = oge.getKitap();
                        kitap.setMevcutAdet(kitap.getMevcutAdet() + oge.getAdet());
                        kitapDepo.save(kitap);
                    }
                    
                    oduncAlmaDepo.save(oduncAlma);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean tekKitapIadeEt(Integer oduncAlmaOgesiNo) {
        return oduncAlmaOgesiDepo.findById(oduncAlmaOgesiNo)
                .map(oge -> {
                    oge.setIadeTarihi(LocalDateTime.now());
                    oge.setDurum("TESLIM_EDILDI");
                    oduncAlmaOgesiDepo.save(oge);
                    
                    Kitap kitap = oge.getKitap();
                    kitap.setMevcutAdet(kitap.getMevcutAdet() + oge.getAdet());
                    kitapDepo.save(kitap);
                    
                    OduncAlma oduncAlma = oge.getOduncAlma();
                    List<OduncAlmaOgesi> ogeler = oduncAlmaOgesiDepo.findByOduncAlmaNo(oduncAlma.getNo());
                    
                    boolean tumunuTeslimEtti = true;
                    for (OduncAlmaOgesi digerOge : ogeler) {
                        if (!"TESLIM_EDILDI".equals(digerOge.getDurum())) {
                            tumunuTeslimEtti = false;
                            break;
                        }
                    }
                    
                    if (tumunuTeslimEtti) {
                        oduncAlma.setDurum("TAMAMLANDI");
                        oduncAlmaDepo.save(oduncAlma);
                    }
                    
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public int cezaHesapla(Integer oduncAlmaNo) {
        return oduncAlmaDepo.findById(oduncAlmaNo)
                .map(oduncAlma -> {
                    if (!"AKTIF".equals(oduncAlma.getDurum())) {
                        return 0;
                    }
                    
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isBefore(oduncAlma.getSonTeslimTarihi())) {
                        return 0;
                    }
                    
                    long gunSayisi = ChronoUnit.DAYS.between(oduncAlma.getSonTeslimTarihi(), now);
                    int gunlukCeza = 5; // 5 TL günlük ceza
                    return (int) (gunSayisi * gunlukCeza * oduncAlma.getToplamOdunc());
                })
                .orElse(0);
    }

    public List<OduncAlmaOgesi> oduncAlmaOgeleriniGetir(Integer oduncAlmaNo) {
        return oduncAlmaOgesiDepo.findByOduncAlmaNo(oduncAlmaNo);
    }
}
