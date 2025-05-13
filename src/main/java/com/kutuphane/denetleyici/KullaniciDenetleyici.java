package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KullaniciHizmet;
import com.kutuphane.hizmet.OdemeHizmet;
import com.kutuphane.hizmet.OduncAlmaHizmet;
import com.kutuphane.varlik.Kullanici;
import com.kutuphane.varlik.OduncAlma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kullanicilar")
public class KullaniciDenetleyici {

    private final KullaniciHizmet kullaniciHizmet;
    private final OduncAlmaHizmet oduncAlmaHizmet;
    private final OdemeHizmet odemeHizmet;

    @Autowired
    public KullaniciDenetleyici(KullaniciHizmet kullaniciHizmet, OduncAlmaHizmet oduncAlmaHizmet, OdemeHizmet odemeHizmet) {
        this.kullaniciHizmet = kullaniciHizmet;
        this.oduncAlmaHizmet = oduncAlmaHizmet;
        this.odemeHizmet = odemeHizmet;
    }

    @GetMapping
    public String kullanicilariGoster(Model model, @RequestParam(required = false) String arama) {
        List<Kullanici> kullanicilar;
        
        if (arama != null && !arama.isEmpty()) {
            kullanicilar = kullaniciHizmet.kullaniciAra(arama);
            model.addAttribute("arama", arama);
        } else {
            kullanicilar = kullaniciHizmet.tumKullanicilariGetir();
        }
        
        model.addAttribute("kullanicilar", kullanicilar);
        return "kullanicilar";
    }

    @GetMapping("/{no}")
    public String kullaniciDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Kullanici> kullanici = kullaniciHizmet.kullaniciGetir(no);
        
        if (kullanici.isPresent()) {
            model.addAttribute("kullanici", kullanici.get());
            
            List<OduncAlma> oduncAlmalar = oduncAlmaHizmet.kullaniciOduncAlmalariGetir(no);
            model.addAttribute("oduncAlmalar", oduncAlmalar);
            
            Integer toplamCeza = odemeHizmet.kullaniciCezaToplaminiGetir(no);
            model.addAttribute("toplamCeza", toplamCeza != null ? toplamCeza : 0);
            
            return "kullanici-detay";
        } else {
            return "redirect:/kullanicilar";
        }
    }

    @GetMapping("/ekle")
    public String kullaniciEkleFormuGoster(Model model) {
        model.addAttribute("kullanici", new Kullanici());
        return "kullanici-ekle";
    }

    @PostMapping("/ekle")
    public String kullaniciEkle(@ModelAttribute Kullanici kullanici, RedirectAttributes redirectAttributes) {
        try {
            Kullanici eklenenKullanici = kullaniciHizmet.kullaniciEkle(kullanici);
            redirectAttributes.addFlashAttribute("mesaj", "Kullanıcı başarıyla eklendi: " + eklenenKullanici.getAd() + " " + eklenenKullanici.getSoyad());
            return "redirect:/kullanicilar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kullanıcı eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/kullanicilar/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String kullaniciDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Kullanici> kullanici = kullaniciHizmet.kullaniciGetir(no);
        
        if (kullanici.isPresent()) {
            model.addAttribute("kullanici", kullanici.get());
            return "kullanici-duzenle";
        } else {
            return "redirect:/kullanicilar";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String kullaniciDuzenle(@PathVariable Integer no, @ModelAttribute Kullanici kullanici, RedirectAttributes redirectAttributes) {
        try {
            Kullanici guncellenenKullanici = kullaniciHizmet.kullaniciGuncelle(no, kullanici);
            redirectAttributes.addFlashAttribute("mesaj", "Kullanıcı başarıyla güncellendi: " + guncellenenKullanici.getAd() + " " + guncellenenKullanici.getSoyad());
            return "redirect:/kullanicilar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kullanıcı güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/kullanicilar/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String kullaniciSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Kullanici> kullanici = kullaniciHizmet.kullaniciGetir(no);
            if (kullanici.isPresent()) {
                kullaniciHizmet.kullaniciSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Kullanıcı başarıyla silindi: " + kullanici.get().getAd() + " " + kullanici.get().getSoyad());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Kullanıcı bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kullanıcı silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/kullanicilar";
    }

    @PostMapping("/durum/{no}")
    public String kullaniciDurumGuncelle(@PathVariable Integer no, @RequestParam String durum, RedirectAttributes redirectAttributes) {
        try {
            kullaniciHizmet.durumGuncelle(no, durum);
            redirectAttributes.addFlashAttribute("mesaj", "Kullanıcı durumu başarıyla güncellendi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kullanıcı durumu güncellenirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/kullanicilar/" + no;
    }
}
