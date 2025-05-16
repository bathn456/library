package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KitaplikHizmet;
import com.kutuphane.hizmet.RafHizmet;
import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Raf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/raflar")
public class RafDenetleyici {

    private final RafHizmet rafHizmet;
    private final KitaplikHizmet kitaplikHizmet;

    @Autowired
    public RafDenetleyici(RafHizmet rafHizmet, KitaplikHizmet kitaplikHizmet) {
        this.rafHizmet = rafHizmet;
        this.kitaplikHizmet = kitaplikHizmet;
    }

    @GetMapping
    public String raflariGoster(Model model, @RequestParam(required = false) String arama) {
        List<Raf> raflar;
        
        if (arama != null && !arama.isEmpty()) {
            raflar = rafHizmet.rafAra(arama);
            model.addAttribute("arama", arama);
        } else {
            raflar = rafHizmet.tumRaflariGetir();
        }
        
        model.addAttribute("raflar", raflar);
        return "raflar";
    }

    @GetMapping("/{no}")
    public String rafDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Raf> raf = rafHizmet.rafGetir(no);
        
        if (raf.isPresent()) {
            model.addAttribute("raf", raf.get());
            return "raf-detay";
        } else {
            return "redirect:/raflar";
        }
    }

    @GetMapping("/ekle")
    public String rafEkleFormuGoster(Model model) {
        model.addAttribute("raf", new Raf());
        model.addAttribute("kitapliklar", kitaplikHizmet.tumKitapliklariGetir());
        return "raf-ekle";
    }

    @PostMapping("/ekle")
    public String rafEkle(@ModelAttribute Raf raf, RedirectAttributes redirectAttributes) {
        try {
            Raf eklenenRaf = rafHizmet.rafEkle(raf);
            redirectAttributes.addFlashAttribute("mesaj", "Raf başarıyla eklendi: " + eklenenRaf.getIsim());
            return "redirect:/raflar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Raf eklenirken hata oluştu: " + e.getMessage());
            return "redirect:/raflar/ekle";
        }
    }

    @GetMapping("/duzenle/{no}")
    public String rafDuzenleFormuGoster(@PathVariable Integer no, Model model) {
        Optional<Raf> raf = rafHizmet.rafGetir(no);
        
        if (raf.isPresent()) {
            model.addAttribute("raf", raf.get());
            model.addAttribute("kitapliklar", kitaplikHizmet.tumKitapliklariGetir());
            return "raf-duzenle";
        } else {
            return "redirect:/raflar";
        }
    }

    @PostMapping("/duzenle/{no}")
    public String rafDuzenle(@PathVariable Integer no, @ModelAttribute Raf raf, RedirectAttributes redirectAttributes) {
        try {
            Raf guncellenenRaf = rafHizmet.rafGuncelle(no, raf);
            redirectAttributes.addFlashAttribute("mesaj", "Raf başarıyla güncellendi: " + guncellenenRaf.getIsim());
            return "redirect:/raflar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Raf güncellenirken hata oluştu: " + e.getMessage());
            return "redirect:/raflar/duzenle/" + no;
        }
    }

    @GetMapping("/sil/{no}")
    public String rafSil(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            Optional<Raf> raf = rafHizmet.rafGetir(no);
            if (raf.isPresent()) {
                rafHizmet.rafSil(no);
                redirectAttributes.addFlashAttribute("mesaj", "Raf başarıyla silindi: " + raf.get().getIsim());
            } else {
                redirectAttributes.addFlashAttribute("hata", "Raf bulunamadı: " + no);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "Raf silinirken hata oluştu: " + e.getMessage());
        }
        return "redirect:/raflar";
    }
    
    @GetMapping("/kitaplik/{kitaplikNo}")
    public String kitaplikRaflariniGoster(@PathVariable Integer kitaplikNo, Model model) {
        Optional<Kitaplik> kitaplik = kitaplikHizmet.kitaplikGetir(kitaplikNo);
        
        if (kitaplik.isPresent()) {
            List<Raf> raflar = rafHizmet.kitaplikRaflariniGetir(kitaplik.get());
            model.addAttribute("raflar", raflar);
            model.addAttribute("kitaplik", kitaplik.get());
            return "raflar";
        } else {
            return "redirect:/raflar";
        }
    }
}