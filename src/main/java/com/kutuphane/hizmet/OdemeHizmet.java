package com.kutuphane.hizmet;

import com.kutuphane.depo.CezaOdemesiDepo;
import com.kutuphane.depo.KullaniciDepo;
import com.kutuphane.depo.OdemeDepo;
import com.kutuphane.varlik.CezaOdemesi;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.Odeme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OdemeHizmet {

    private final OdemeDepo odemeDepo;
    private final CezaOdemesiDepo cezaOdemesiDepo;
    private final KullaniciDepo kullaniciDepo;

    @Autowired
    public OdemeHizmet(OdemeDepo odemeDepo, CezaOdemesiDepo cezaOdemesiDepo, 
                      KullaniciDepo kullaniciDepo) {
        this.odemeDepo = odemeDepo;
        this.cezaOdemesiDepo = cezaOdemesiDepo;
        this.kullaniciDepo = kullaniciDepo;
    }

    public List<Odeme> tumOdemeleriGetir() {
        return odemeDepo.findAll();
    }

    public Optional<Odeme> odemeGetir(Integer no) {
        return odemeDepo.findById(no);
    }

    public List<Odeme> kullaniciOdemeleriniGetir(Integer kullaniciNo) {
        return odemeDepo.findByKullaniciNo(kullaniciNo);
    }

    public List<Odeme> durumIleGetir(String durum) {
        return odemeDepo.findByDurum(durum);
    }

    public Integer kullaniciCezaToplaminiGetir(Integer kullaniciNo) {
        return cezaOdemesiDepo.getTotalPenaltiesPaidByUser(kullaniciNo);
    }

    @Transactional
    public Odeme cezaOdemesiOlustur(Integer kullaniciNo, Integer tutar, String odemeYontemi, Integer cezaNo) {
        Kullanici kullanici = kullaniciDepo.findById(kullaniciNo)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + kullaniciNo));
        
        Odeme odeme = new Odeme();
        odeme.setKullanici(kullanici);
        odeme.setTutar(tutar);
        odeme.setOdemeTarihi(LocalDateTime.now());
        odeme.setOdemeYontemi(odemeYontemi);
        odeme.setDurum("TAMAMLANDI");
        
        Odeme kaydedilenOdeme = odemeDepo.save(odeme);
        
        CezaOdemesi cezaOdemesi = new CezaOdemesi();
        cezaOdemesi.setOdeme(kaydedilenOdeme);
        cezaOdemesi.setOdenmisTutar(tutar);
        cezaOdemesi.setCezaNo(cezaNo);
        cezaOdemesi.setTarihSaat(LocalDateTime.now());
        cezaOdemesi.setOdendiMi(true);
        
        cezaOdemesiDepo.save(cezaOdemesi);
        
        return kaydedilenOdeme;
    }

    @Transactional
    public boolean odemeIptalEt(Integer odemeNo) {
        return odemeDepo.findById(odemeNo)
                .map(odeme -> {
                    if ("TAMAMLANDI".equals(odeme.getDurum())) {
                        odeme.setDurum("IPTAL_EDILDI");
                        
                        List<CezaOdemesi> cezaOdemeleri = cezaOdemesiDepo.findByOdemeNo(odemeNo);
                        for (CezaOdemesi cezaOdemesi : cezaOdemeleri) {
                            cezaOdemesi.setOdendiMi(false);
                            cezaOdemesiDepo.save(cezaOdemesi);
                        }
                        
                        odemeDepo.save(odeme);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public List<CezaOdemesi> cezaOdemeleriniGetir(Integer odemeNo) {
        return cezaOdemesiDepo.findByOdemeNo(odemeNo);
    }

    public List<CezaOdemesi> odenmemisCezalariGetir() {
        return cezaOdemesiDepo.findByOdendiMi(false);
    }
}
