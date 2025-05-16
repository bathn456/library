package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KategoriHizmet;
import com.kutuphane.hizmet.KitapHizmet;
import com.kutuphane.hizmet.RafHizmet;
import com.kutuphane.varlik.Kategori;
import com.kutuphane.varlik.Kitap;
import com.kutuphane.varlik.Raf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kitaplar")
public class KitapDenetleyici {

    private final KitapHizmet kitapHizmet;
    private final KategoriHizmet kategoriHizmet;
    private final RafHizmet rafHizmet;

    @Autowired
    public KitapDenetleyici(KitapHizmet kitapHizmet, KategoriHizmet kategoriHizmet, RafHizmet rafHizmet) {
        this.kitapHizmet = kitapHizmet;
        this.kategoriHizmet = kategoriHizmet;
        this.rafHizmet = rafHizmet;
    }

    @GetMapping
    public String kitaplariGoster(Model model, @RequestParam(required = false) String arama) {
        List<Kitap> kitaplar;

        if (arama != null && !arama.isEmpty()) {
            kitaplar = kitapHizmet.kitapAra(arama);
            model.addAttribute("arama", arama);
        } else {
            kitaplar = kitapHizmet.tumKitaplariGetir();
        }

        model.addAttribute("kitaplar", kitaplar);
        return "kitaplar";
    }

    @GetMapping("/{no}")
    public String kitapDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Kitap> kitap = kitapHizmet.kitapGetir(no);

        if (kitap.isPresent()) {
            model.addAttribute("kitap", kitap.get());
            return "kitap-detay";
        } else {
            return "redirect:/kitaplar";
        }
    }

    @GetMapping("/ekle")
    public String kitapEkleFormuGoster(Model model) {
        model.addAttribute("kitap", new Kitap());
        model.addAttribute("kategoriler", kategoriHizmet.tumKategorileriGetir());
        model.addAttribute("raflar", rafHizmet.tumRaflariGetir());
        return "kitap-ekle";
    }

    @PostMapping("/ekle")
    public String kitapEkle(@ModelAttribute Kitap kitap, RedirectAttributes redirectAttributes) {
        try {
            Kitap eklenenKitap = kitapHizmet.kitapEkle(kitap);
            redirectAttributes.addFlashAttribute("mesaj", "Kitap başarıyla eklendi: " + eklenenKitap.getBaslik());
            return "redirect:/kitaplar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitap eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/kitaplar/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String kitapDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Kitap> kitap = kitapHizmet.kitapGetir(no);

        if (kitap.isPresent()) {
            model.addAttribute("kitap", kitap.get());
            model.addAttribute("kategoriler", kategoriHizmet.tumKategorileriGetir());
            model.addAttribute("raflar", rafHizmet.tumRaflariGetir());
            return "kitap-duzenle";
        } else {
            return "redirect:/kitaplar";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String kitapDuzenle(@PathVariable Integer no, @ModelAttribute Kitap kitap, RedirectAttributes redirectAttributes) {
        try {
            Kitap guncellenenKitap = kitapHizmet.kitapGuncelle(no, kitap);
            redirectAttributes.addFlashAttribute("mesaj", "Kitap başarıyla güncellendi: " + guncellenenKitap.getBaslik());
            return "redirect:/kitaplar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitap güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/kitaplar/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String kitapSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Kitap> kitap = kitapHizmet.kitapGetir(no);
            if (kitap.isPresent()) {
                kitapHizmet.kitapSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Kitap başarıyla silindi: " + kitap.get().getBaslik());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Kitap bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Kitap silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/kitaplar";
    }

    @GetMapping("/kategori/{kategoriNo}")
    public String kategoriKitaplariGoster(@PathVariable Integer kategoriNo, Model model) {
        List<Kitap> kitaplar = kitapHizmet.kategoriIleGetir(kategoriNo);
        Optional<Kategori> kategori = kategoriHizmet.kategoriGetir(kategoriNo);

        model.addAttribute("kitaplar", kitaplar);
        kategori.ifPresent(k -> model.addAttribute("kategori", k));

        return "kitaplar";
    }

    @GetMapping("/mevcut")
    public String mevcutKitaplariGoster(Model model) {
        model.addAttribute("kitaplar", kitapHizmet.mevutKitaplariGetir());
        model.addAttribute("sadeceMevcut", true);
        return "kitaplar";
    }

    @GetMapping("/kitaplar")
   public String kitaplariListele(Model model) {
     model.addAttribute("kitaplar", kitapHizmet.tumKitaplariGetir());
    return "kitaplar";
   }
}