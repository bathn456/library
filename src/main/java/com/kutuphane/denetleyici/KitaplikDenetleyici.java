package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KitaplikHizmet;
import com.kutuphane.hizmet.OdaHizmet;
import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Oda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kitapliklar")
public class KitaplikDenetleyici {

    private final KitaplikHizmet kitaplikHizmet;
    private final OdaHizmet odaHizmet;

    @Autowired
    public KitaplikDenetleyici(KitaplikHizmet kitaplikHizmet, OdaHizmet odaHizmet) {
        this.kitaplikHizmet = kitaplikHizmet;
        this.odaHizmet = odaHizmet;
    }

    @GetMapping
    public String kitapliklariGoster(Model model, @RequestParam(required = false) String arama) {
        List<Kitaplik> kitapliklar;
        
        if (arama != null && !arama.isEmpty()) {
            kitapliklar = kitaplikHizmet.kitaplikAra(arama);
            model.addAttribute("arama", arama);
        } else {
            kitapliklar = kitaplikHizmet.tumKitapliklariGetir();
        }
        
        model.addAttribute("kitapliklar", kitapliklar);
        return "kitapliklar";
    }

    @GetMapping("/{no}")
    public String kitaplikDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Kitaplik> kitaplik = kitaplikHizmet.kitaplikGetir(no);
        
        if (kitaplik.isPresent()) {
            model.addAttribute("kitaplik", kitaplik.get());
            return "kitaplik-detay";
        } else {
            return "redirect:/kitapliklar";
        }
    }

    @GetMapping("/ekle")
    public String kitaplikEkleFormuGoster(Model model) {
        model.addAttribute("kitaplik", new Kitaplik());
        model.addAttribute("odalar", odaHizmet.tumOdalariGetir());
        return "kitaplik-ekle";
    }

    @PostMapping("/ekle")
    public String kitaplikEkle(@ModelAttribute Kitaplik kitaplik, RedirectAttributes redirectAttributes) {
        try {
            Kitaplik eklenenKitaplik = kitaplikHizmet.kitaplikEkle(kitaplik);
            redirectAttributes.addFlashAttribute("mesaj", "Kitaplık başarıyla eklendi: " + eklenenKitaplik.getIsim());
            return "redirect:/kitapliklar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitaplık eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/kitapliklar/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String kitaplikDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Kitaplik> kitaplik = kitaplikHizmet.kitaplikGetir(no);
        
        if (kitaplik.isPresent()) {
            model.addAttribute("kitaplik", kitaplik.get());
            model.addAttribute("odalar", odaHizmet.tumOdalariGetir());
            return "kitaplik-duzenle";
        } else {
            return "redirect:/kitapliklar";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String kitaplikDuzenle(@PathVariable Integer no, @ModelAttribute Kitaplik kitaplik, RedirectAttributes redirectAttributes) {
        try {
            Kitaplik guncellenenKitaplik = kitaplikHizmet.kitaplikGuncelle(no, kitaplik);
            redirectAttributes.addFlashAttribute("mesaj", "Kitaplık başarıyla güncellendi: " + guncellenenKitaplik.getIsim());
            return "redirect:/kitapliklar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitaplık güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/kitapliklar/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String kitaplikSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Kitaplik> kitaplik = kitaplikHizmet.kitaplikGetir(no);
            if (kitaplik.isPresent()) {
                kitaplikHizmet.kitaplikSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Kitaplık başarıyla silindi: " + kitaplik.get().getIsim());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Kitaplık bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitaplık silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/kitapliklar";
    }
    
    @GetMapping("/oda/{odaNo}")
    public String odaKitapliklariniGoster(@PathVariable Integer odaNo, Model model) {
        Optional<Oda> oda = odaHizmet.odaGetir(odaNo);
        
        if (oda.isPresent()) {
            List<Kitaplik> kitapliklar = kitaplikHizmet.odaKitapliklariniGetir(oda.get());
            model.addAttribute("kitapliklar", kitapliklar);
            model.addAttribute("oda", oda.get());
            return "kitapliklar";
        } else {
            return "redirect:/kitapliklar";
        }
    }
}