package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KitapHizmet;
import com.kutuphane.hizmet.KullaniciHizmet;
import com.kutuphane.hizmet.RezervasyonHizmet;
import com.kutuphane.varlik.Rezervasyon;
import com.kutuphane.varlik.RezervasyonOgesi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rezervasyonlar")
public class RezervasyonDenetleyici {

    private final RezervasyonHizmet rezervasyonHizmet;
    private final KitapHizmet kitapHizmet;
    private final KullaniciHizmet kullaniciHizmet;

    @Autowired
    public RezervasyonDenetleyici(RezervasyonHizmet rezervasyonHizmet, KitapHizmet kitapHizmet,
                                KullaniciHizmet kullaniciHizmet) {
        this.rezervasyonHizmet = rezervasyonHizmet;
        this.kitapHizmet = kitapHizmet;
        this.kullaniciHizmet = kullaniciHizmet;
    }

    @GetMapping
    public String rezervasyonlariGoster(Model model, @RequestParam(required = false) String durum) {
        List<Rezervasyon> rezervasyonlar;
        
        if (durum != null && !durum.isEmpty()) {
            rezervasyonlar = rezervasyonHizmet.durumIleGetir(durum);
            model.addAttribute("durum", durum);
        } else {
            rezervasyonlar = rezervasyonHizmet.tumRezervasyonlariGetir();
        }
        
        model.addAttribute("rezervasyonlar", rezervasyonlar);
        return "rezervasyonlar";
    }

    @GetMapping("/{no}")
    public String rezervasyonDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Rezervasyon> rezervasyon = rezervasyonHizmet.rezervasyonGetir(no);
        
        if (rezervasyon.isPresent()) {
            model.addAttribute("rezervasyon", rezervasyon.get());
            
            List<RezervasyonOgesi> rezervasyonOgeleri = rezervasyonHizmet.rezervasyonOgeleriniGetir(no);
            model.addAttribute("rezervasyonOgeleri", rezervasyonOgeleri);
            
            return "rezervasyon-detay";
        } else {
            return "redirect:/rezervasyonlar";
        }
    }

    @GetMapping("/ekle")
    public String rezervasyonEkleFormuGoster(Model model) {
        model.addAttribute("kullanicilar", kullaniciHizmet.tumKullanicilariGetir());
        model.addAttribute("kitaplar", kitapHizmet.tumKitaplariGetir());
        return "rezervasyon-ekle";
    }

    @PostMapping("/ekle")
    public String rezervasyonEkle(@RequestParam Integer kullaniciNo, 
                                @RequestParam List<Integer> kitapNoLari,
                                @RequestParam List<Integer> adetler,
                                RedirectAttributes redirectAttributes) {
        try {
            Rezervasyon eklenenRezervasyon = rezervasyonHizmet.rezervasyonOlustur(kullaniciNo, kitapNoLari, adetler);
            redirectAttributes.addFlashAttribute("mesaj", "Rezervasyon başarıyla oluşturuldu. No: " + eklenenRezervasyon.getNo());
            return "redirect:/rezervasyonlar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Rezervasyon oluşturulurken hata oluştu: " + e.getMessage());
            return "redirect:/rezervasyonlar/ekle";
        }
    }

    @PostMapping("/iptal/{no}")
    public String rezervasyonIptalEt(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            boolean sonuc = rezervasyonHizmet.rezervasyonIptalEt(no);
            if (sonuc) {
                redirectAttributes.addFlashAttribute("mesaj", "Rezervasyon başarıyla iptal edildi.");
            } else {
                redirectAttributes.addFlashAttribute("hata", "Rezervasyon bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "İptal işlemi sırasında hata oluştu: " + e.getMessage());
        }
        return "redirect:/rezervasyonlar/" + no;
    }

    @PostMapping("/onayla/{no}")
    public String rezervasyonuOnayla(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            boolean sonuc = rezervasyonHizmet.rezervasyonuOnayla(no);
            if (sonuc) {
                redirectAttributes.addFlashAttribute("mesaj", "Rezervasyon başarıyla onaylandı.");
            } else {
                redirectAttributes.addFlashAttribute("hata", "Rezervasyon bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Onaylama işlemi sırasında hata oluştu: " + e.getMessage());
        }
        return "redirect:/rezervasyonlar/" + no;
    }

    @GetMapping("/suresi-gecmis")
    public String suresiGecmisRezervasyonlariGoster(Model model) {
        model.addAttribute("rezervasyonlar", rezervasyonHizmet.suresiGecmisRezervasyonlariGetir());
        model.addAttribute("suresiGecmis", true);
        return "rezervasyonlar";
    }
}
