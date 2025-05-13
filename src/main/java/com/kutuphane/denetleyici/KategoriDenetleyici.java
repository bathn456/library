package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KategoriHizmet;
import com.kutuphane.varlik.Kategori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kategoriler")
public class KategoriDenetleyici {

    private final KategoriHizmet kategoriHizmet;

    @Autowired
    public KategoriDenetleyici(KategoriHizmet kategoriHizmet) {
        this.kategoriHizmet = kategoriHizmet;
    }

    @GetMapping
    public String kategorileriGoster(Model model, @RequestParam(required = false) String arama) {
        List<Kategori> kategoriler;
        
        if (arama != null && !arama.isEmpty()) {
            kategoriler = kategoriHizmet.kategoriAra(arama);
            model.addAttribute("arama", arama);
        } else {
            kategoriler = kategoriHizmet.tumKategorileriGetir();
        }
        
        model.addAttribute("kategoriler", kategoriler);
        return "kategoriler";
    }

    @GetMapping("/{no}")
    public String kategoriDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Kategori> kategori = kategoriHizmet.kategoriGetir(no);
        
        if (kategori.isPresent()) {
            model.addAttribute("kategori", kategori.get());
            
            if (kategori.get().getUstKategoriNo() != null) {
                kategoriHizmet.kategoriGetir(kategori.get().getUstKategoriNo())
                    .ifPresent(ustKategori -> model.addAttribute("ustKategori", ustKategori));
            }
            
            List<Kategori> altKategoriler = kategoriHizmet.altKategorileriGetir(no);
            model.addAttribute("altKategoriler", altKategoriler);
            
            return "kategori-detay";
        } else {
            return "redirect:/kategoriler";
        }
    }

    @GetMapping("/ekle")
    public String kategoriEkleFormuGoster(Model model) {
        model.addAttribute("kategori", new Kategori());
        model.addAttribute("ustKategoriler", kategoriHizmet.tumKategorileriGetir());
        return "kategori-ekle";
    }

    @PostMapping("/ekle")
    public String kategoriEkle(@ModelAttribute Kategori kategori, RedirectAttributes redirectAttributes) {
        try {
            Kategori eklenenKategori = kategoriHizmet.kategoriEkle(kategori);
            redirectAttributes.addFlashAttribute("mesaj", "Kategori başarıyla eklendi: " + eklenenKategori.getIsim());
            return "redirect:/kategoriler";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kategori eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/kategoriler/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String kategoriDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Kategori> kategori = kategoriHizmet.kategoriGetir(no);
        
        if (kategori.isPresent()) {
            model.addAttribute("kategori", kategori.get());
            model.addAttribute("ustKategoriler", kategoriHizmet.tumKategorileriGetir());
            return "kategori-duzenle";
        } else {
            return "redirect:/kategoriler";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String kategoriDuzenle(@PathVariable Integer no, @ModelAttribute Kategori kategori, RedirectAttributes redirectAttributes) {
        try {
            Kategori guncellenenKategori = kategoriHizmet.kategoriGuncelle(no, kategori);
            redirectAttributes.addFlashAttribute("mesaj", "Kategori başarıyla güncellendi: " + guncellenenKategori.getIsim());
            return "redirect:/kategoriler";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kategori güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/kategoriler/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String kategoriSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Kategori> kategori = kategoriHizmet.kategoriGetir(no);
            if (kategori.isPresent()) {
                kategoriHizmet.kategoriSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Kategori başarıyla silindi: " + kategori.get().getIsim());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Kategori bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kategori silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/kategoriler";
    }

    @GetMapping("/ust-kategoriler")
    public String ustKategorileriGoster(Model model) {
        model.addAttribute("kategoriler", kategoriHizmet.ustKategorileriGetir());
        model.addAttribute("sadecaUstKategoriler", true);
        return "kategoriler";
    }
}
