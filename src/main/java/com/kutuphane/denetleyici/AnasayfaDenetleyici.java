package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KitapHizmet;
import com.kutuphane.hizmet.KullaniciHizmet;
import com.kutuphane.hizmet.OduncAlmaHizmet;
import com.kutuphane.hizmet.RezervasyonHizmet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnasayfaDenetleyici {

    private final KitapHizmet kitapHizmet;
    private final KullaniciHizmet kullaniciHizmet;
    private final OduncAlmaHizmet oduncAlmaHizmet;
    private final RezervasyonHizmet rezervasyonHizmet;

    @Autowired
    public AnasayfaDenetleyici(KitapHizmet kitapHizmet, KullaniciHizmet kullaniciHizmet,
                           OduncAlmaHizmet oduncAlmaHizmet, RezervasyonHizmet rezervasyonHizmet) {
        this.kitapHizmet = kitapHizmet;
        this.kullaniciHizmet = kullaniciHizmet;
        this.oduncAlmaHizmet = oduncAlmaHizmet;
        this.rezervasyonHizmet = rezervasyonHizmet;
    }

    @GetMapping("/")
    public String anasayfa(Model model) {
        int toplamKitapSayisi = kitapHizmet.tumKitaplariGetir().size();
        int mevcutKitapSayisi = kitapHizmet.mevutKitaplariGetir().size();
        int toplamKullaniciSayisi = kullaniciHizmet.tumKullanicilariGetir().size();
        int aktifOduncSayisi = oduncAlmaHizmet.durumIleGetir("AKTIF").size();
        int bekleyenRezervasyonSayisi = rezervasyonHizmet.durumIleGetir("BEKLIYOR").size();
        int suresiGecmisOduncSayisi = oduncAlmaHizmet.süresiGecmisOduncAlmalariGetir().size();
        
        model.addAttribute("toplamKitapSayisi", toplamKitapSayisi);
        model.addAttribute("mevcutKitapSayisi", mevcutKitapSayisi);
        model.addAttribute("toplamKullaniciSayisi", toplamKullaniciSayisi);
        model.addAttribute("aktifOduncSayisi", aktifOduncSayisi);
        model.addAttribute("bekleyenRezervasyonSayisi", bekleyenRezervasyonSayisi);
        model.addAttribute("suresiGecmisOduncSayisi", suresiGecmisOduncSayisi);
        
        model.addAttribute("sonKitaplar", kitapHizmet.tumKitaplariGetir());
        model.addAttribute("aktifOduncler", oduncAlmaHizmet.durumIleGetir("AKTIF"));
        
        return "anasayfa";
    }
}
