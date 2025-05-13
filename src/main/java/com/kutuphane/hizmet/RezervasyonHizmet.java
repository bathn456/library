package com.kutuphane.hizmet;

import com.kutuphane.depo.KitapDepo;
import com.kutuphane.depo.KullaniciDepo;
import com.kutuphane.depo.RezervasyonDepo;
import com.kutuphane.depo.RezervasyonOgesiDepo;
import com.kutuphane.varlik.Kitap;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.Rezervasyon;
import com.kutuphane.varlik.RezervasyonOgesi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RezervasyonHizmet {

    private final RezervasyonDepo rezervasyonDepo;
    private final RezervasyonOgesiDepo rezervasyonOgesiDepo;
    private final KitapDepo kitapDepo;
    private final KullaniciDepo kullaniciDepo;

    @Autowired
    public RezervasyonHizmet(RezervasyonDepo rezervasyonDepo, RezervasyonOgesiDepo rezervasyonOgesiDepo,
                            KitapDepo kitapDepo, KullaniciDepo kullaniciDepo) {
        this.rezervasyonDepo = rezervasyonDepo;
        this.rezervasyonOgesiDepo = rezervasyonOgesiDepo;
        this.kitapDepo = kitapDepo;
        this.kullaniciDepo = kullaniciDepo;
    }

    public List<Rezervasyon> tumRezervasyonlariGetir() {
        return rezervasyonDepo.findAll();
    }

    public Optional<Rezervasyon> rezervasyonGetir(Integer no) {
        return rezervasyonDepo.findById(no);
    }

    public List<Rezervasyon> kullaniciRezervasyonlariniGetir(Integer kullaniciNo) {
        return rezervasyonDepo.findByKullaniciNo(kullaniciNo);
    }

    public List<Rezervasyon> durumIleGetir(String durum) {
        return rezervasyonDepo.findByDurum(durum);
    }

    public List<Rezervasyon> suresiGecmisRezervasyonlariGetir() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return rezervasyonDepo.findExpiredReservations(threeDaysAgo);
    }

    @Transactional
    public Rezervasyon rezervasyonOlustur(Integer kullaniciNo, List<Integer> kitapNoLari, List<Integer> adetler) {
        if (kitapNoLari.size() != adetler.size()) {
            throw new IllegalArgumentException("Kitap ve adet listeleri aynı boyutta olmalıdır.");
        }
        
        Kullanici kullanici = kullaniciDepo.findById(kullaniciNo)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + kullaniciNo));
        
        Rezervasyon rezervasyon = new Rezervasyon();
        rezervasyon.setKullanici(kullanici);
        rezervasyon.setRezervasyonTarihi(LocalDateTime.now());
        rezervasyon.setDurum("BEKLIYOR");
        
        Rezervasyon kaydedilenRezervasyon = rezervasyonDepo.save(rezervasyon);
        
        for (int i = 0; i < kitapNoLari.size(); i++) {
            Integer kitapNo = kitapNoLari.get(i);
            Integer adet = adetler.get(i);
            
            Kitap kitap = kitapDepo.findById(kitapNo)
                    .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + kitapNo));
            
            RezervasyonOgesi rezervasyonOgesi = new RezervasyonOgesi();
            rezervasyonOgesi.setRezervasyon(kaydedilenRezervasyon);
            rezervasyonOgesi.setKitap(kitap);
            rezervasyonOgesi.setAdet(adet);
            rezervasyonOgesi.setDurum("BEKLIYOR");
            
            rezervasyonOgesiDepo.save(rezervasyonOgesi);
        }
        
        return kaydedilenRezervasyon;
    }

    @Transactional
    public boolean rezervasyonIptalEt(Integer rezervasyonNo) {
        return rezervasyonDepo.findById(rezervasyonNo)
                .map(rezervasyon -> {
                    rezervasyon.setDurum("IPTAL_EDILDI");
                    
                    List<RezervasyonOgesi> ogeler = rezervasyonOgesiDepo.findByRezervasyonNo(rezervasyonNo);
                    for (RezervasyonOgesi oge : ogeler) {
                        oge.setDurum("IPTAL_EDILDI");
                        rezervasyonOgesiDepo.save(oge);
                    }
                    
                    rezervasyonDepo.save(rezervasyon);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean rezervasyonuOnayla(Integer rezervasyonNo) {
        return rezervasyonDepo.findById(rezervasyonNo)
                .map(rezervasyon -> {
                    rezervasyon.setDurum("ONAYLANDI");
                    
                    List<RezervasyonOgesi> ogeler = rezervasyonOgesiDepo.findByRezervasyonNo(rezervasyonNo);
                    for (RezervasyonOgesi oge : ogeler) {
                        oge.setDurum("ONAYLANDI");
                        rezervasyonOgesiDepo.save(oge);
                    }
                    
                    rezervasyonDepo.save(rezervasyon);
                    return true;
                })
                .orElse(false);
    }

    public List<RezervasyonOgesi> rezervasyonOgeleriniGetir(Integer rezervasyonNo) {
        return rezervasyonOgesiDepo.findByRezervasyonNo(rezervasyonNo);
    }
}
