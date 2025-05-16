package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.OdaHizmet;
import com.kutuphane.varlik.Oda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/odalar")
public class OdaDenetleyici {

    private final OdaHizmet odaHizmet;

    @Autowired
    public OdaDenetleyici(OdaHizmet odaHizmet) {
        this.odaHizmet = odaHizmet;
    }

    @GetMapping
    public String odalariGoster(Model model, @RequestParam(required = false) String arama) {
        List<Oda> odalar;
        
        if (arama != null && !arama.isEmpty()) {
            odalar = odaHizmet.odaAra(arama);
            model.addAttribute("arama", arama);
        } else {
            odalar = odaHizmet.tumOdalariGetir();
        }
        
        model.addAttribute("odalar", odalar);
        return "odalar";
    }

    @GetMapping("/{no}")
    public String odaDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Oda> oda = odaHizmet.odaGetir(no);
        
        if (oda.isPresent()) {
            model.addAttribute("oda", oda.get());
            return "oda-detay";
        } else {
            return "redirect:/odalar";
        }
    }

    @GetMapping("/ekle")
    public String odaEkleFormuGoster(Model model) {
        model.addAttribute("oda", new Oda());
        return "oda-ekle";
    }

    @PostMapping("/ekle")
    public String odaEkle(@ModelAttribute Oda oda, RedirectAttributes redirectAttributes) {
        try {
            Oda eklenenOda = odaHizmet.odaEkle(oda);
            redirectAttributes.addFlashAttribute("mesaj", "Oda başarıyla eklendi: " + eklenenOda.getIsim());
            return "redirect:/odalar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Oda eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/odalar/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String odaDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Oda> oda = odaHizmet.odaGetir(no);
        
        if (oda.isPresent()) {
            model.addAttribute("oda", oda.get());
            return "oda-duzenle";
        } else {
            return "redirect:/odalar";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String odaDuzenle(@PathVariable Integer no, @ModelAttribute Oda oda, RedirectAttributes redirectAttributes) {
        try {
            Oda guncellenenOda = odaHizmet.odaGuncelle(no, oda);
            redirectAttributes.addFlashAttribute("mesaj", "Oda başarıyla güncellendi: " + guncellenenOda.getIsim());
            return "redirect:/odalar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Oda güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/odalar/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String odaSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Oda> oda = odaHizmet.odaGetir(no);
            if (oda.isPresent()) {
                odaHizmet.odaSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Oda başarıyla silindi: " + oda.get().getIsim());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Oda bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Oda silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/odalar";
    }
}