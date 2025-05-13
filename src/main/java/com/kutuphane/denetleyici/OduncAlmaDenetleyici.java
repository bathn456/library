package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KitapHizmet;
import com.kutuphane.hizmet.KullaniciHizmet;
import com.kutuphane.hizmet.OdemeHizmet;
import com.kutuphane.hizmet.OduncAlmaHizmet;
import com.kutuphane.varlik.Kitap;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.OduncAlma;
import com.kutuphane.varlik.OduncAlmaOgesi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/odunc-islemleri")
public class OduncAlmaDenetleyici {

    private final OduncAlmaHizmet oduncAlmaHizmet;
    private final KitapHizmet kitapHizmet;
    private final KullaniciHizmet kullaniciHizmet;
    private final OdemeHizmet odemeHizmet;

    @Autowired
    public OduncAlmaDenetleyici(OduncAlmaHizmet oduncAlmaHizmet, KitapHizmet kitapHizmet,
                              KullaniciHizmet kullaniciHizmet, OdemeHizmet odemeHizmet) {
        this.oduncAlmaHizmet = oduncAlmaHizmet;
        this.kitapHizmet = kitapHizmet;
        this.kullaniciHizmet = kullaniciHizmet;
        this.odemeHizmet = odemeHizmet;
    }

    @GetMapping
    public String oduncAlmalariGoster(Model model, @RequestParam(required = false) String durum) {
        List<OduncAlma> oduncAlmalar;
        
        if (durum != null && !durum.isEmpty()) {
            oduncAlmalar = oduncAlmaHizmet.durumIleGetir(durum);
            model.addAttribute("durum", durum);
        } else {
            oduncAlmalar = oduncAlmaHizmet.tumOduncAlmalariGetir();
        }
        
        model.addAttribute("oduncAlmalar", oduncAlmalar);
        return "odunc-islemleri";
    }

    @GetMapping("/{no}")
    public String oduncAlmaDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<OduncAlma> oduncAlma = oduncAlmaHizmet.oduncAlmaGetir(no);
        
        if (oduncAlma.isPresent()) {
            model.addAttribute("oduncAlma", oduncAlma.get());
            
            List<OduncAlmaOgesi> oduncAlmaOgeleri = oduncAlmaHizmet.oduncAlmaOgeleriniGetir(no);
            model.addAttribute("oduncAlmaOgeleri", oduncAlmaOgeleri);
            
            int cezaUcreti = oduncAlmaHizmet.cezaHesapla(no);
            model.addAttribute("cezaUcreti", cezaUcreti);
            
            return "odunc-alma-detay";
        } else {
            return "redirect:/odunc-islemleri";
        }
    }

    @GetMapping("/ekle")
    public String oduncAlmaEkleFormuGoster(Model model) {
        model.addAttribute("kullanicilar", kullaniciHizmet.tumKullanicilariGetir());
        model.addAttribute("kitaplar", kitapHizmet.mevutKitaplariGetir());
        return "odunc-alma-ekle";
    }

    @PostMapping("/ekle")
    public String oduncAlmaEkle(@RequestParam Integer kullaniciNo, 
                              @RequestParam List<Integer> kitapNoLari,
                              @RequestParam List<Integer> adetler,
                              RedirectAttributes redirectAttributes) {
        try {
            OduncAlma eklenenOduncAlma = oduncAlmaHizmet.oduncAlmaOlustur(kullaniciNo, kitapNoLari, adetler);
            redirectAttributes.addFlashAttribute("mesaj", "Ödünç alma işlemi başarıyla oluşturuldu. No: " + eklenenOduncAlma.getNo());
            return "redirect:/odunc-islemleri";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Ödünç alma işlemi oluşturulurken hata oluştu: " + e.getMessage());
            return "redirect:/odunc-islemleri/ekle";
        }
    }

    @PostMapping("/iade/{no}")
    public String oduncAlmaIadeEt(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            boolean sonuc = oduncAlmaHizmet.oduncAlmaIadeEt(no);
            if (sonuc) {
                redirectAttributes.addFlashAttribute("mesaj", "Ödünç alınan kitaplar başarıyla iade edildi.");
            } else {
                redirectAttributes.addFlashAttribute("hata", "Ödünç alma işlemi bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "İade işlemi sırasında hata oluştu: " + e.getMessage());
        }
        return "redirect:/odunc-islemleri/" + no;
    }

    @PostMapping("/oge-iade/{ogeNo}")
    public String tekOgeIadeEt(@PathVariable Integer ogeNo, @RequestParam Integer oduncAlmaNo, RedirectAttributes redirectAttributes) {
        try {
            boolean sonuc = oduncAlmaHizmet.tekKitapIadeEt(ogeNo);
            if (sonuc) {
                redirectAttributes.addFlashAttribute("mesaj", "Kitap başarıyla iade edildi.");
            } else {
                redirectAttributes.addFlashAttribute("hata", "Ödünç alma ögesi bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "İade işlemi sırasında hata oluştu: " + e.getMessage());
        }
        return "redirect:/odunc-islemleri/" + oduncAlmaNo;
    }

    @GetMapping("/ceza-ode/{oduncAlmaNo}")
    public String cezaOdemeFormuGoster(@PathVariable Integer oduncAlmaNo, Model model) {
        Optional<OduncAlma> oduncAlma = oduncAlmaHizmet.oduncAlmaGetir(oduncAlmaNo);
        
        if (oduncAlma.isPresent()) {
            int cezaUcreti = oduncAlmaHizmet.cezaHesapla(oduncAlmaNo);
            
            model.addAttribute("oduncAlma", oduncAlma.get());
            model.addAttribute("cezaUcreti", cezaUcreti);
            model.addAttribute("odemeYontemleri", List.of("NAKIT", "KREDI_KARTI", "BANKA_KARTI"));
            
            return "ceza-ode";
        } else {
            return "redirect:/odunc-islemleri";
        }
    }

    @PostMapping("/ceza-ode/{oduncAlmaNo}")
    public String cezaOde(@PathVariable Integer oduncAlmaNo,
                         @RequestParam String odemeYontemi,
                         @RequestParam Integer tutar,
                         RedirectAttributes redirectAttributes) {
        try {
            Optional<OduncAlma> oduncAlma = oduncAlmaHizmet.oduncAlmaGetir(oduncAlmaNo);
            
            if (oduncAlma.isPresent()) {
                odemeHizmet.cezaOdemesiOlustur(oduncAlma.get().getKullanici().getNo(), tutar, odemeYontemi, oduncAlmaNo);
                redirectAttributes.addFlashAttribute("mesaj", "Ceza ödemesi başarıyla kaydedildi.");
                return "redirect:/odunc-islemleri/" + oduncAlmaNo;
            } else {
                redirectAttributes.addFlashAttribute("hata", "Ödünç alma işlemi bulunamadı.");
                return "redirect:/odunc-islemleri";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Ceza ödeme işlemi sırasında hata oluştu: " + e.getMessage());
            return "redirect:/odunc-islemleri/ceza-ode/" + oduncAlmaNo;
        }
    }

    @GetMapping("/suresi-gecmis")
    public String suresiGecmisOduncAlmalariGoster(Model model) {
        model.addAttribute("oduncAlmalar", oduncAlmaHizmet.süresiGecmisOduncAlmalariGetir());
        model.addAttribute("suresiGecmis", true);
        return "odunc-islemleri";
    }
}
