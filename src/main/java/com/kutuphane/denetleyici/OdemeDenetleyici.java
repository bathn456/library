package com.kutuphane.denetleyici;

import com.kutuphane.hizmet.KullaniciHizmet;
import com.kutuphane.hizmet.OdemeHizmet;
import com.kutuphane.varlik.CezaOdemesi;
import com.kutuphane.varlik.Odeme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/odemeler")
public class OdemeDenetleyici {

    private final OdemeHizmet odemeHizmet;
    private final KullaniciHizmet kullaniciHizmet;

    @Autowired
    public OdemeDenetleyici(OdemeHizmet odemeHizmet, KullaniciHizmet kullaniciHizmet) {
        this.odemeHizmet = odemeHizmet;
        this.kullaniciHizmet = kullaniciHizmet;
    }

    @GetMapping
    public String odemeleriGoster(Model model, @RequestParam(required = false) String durum) {
        List<Odeme> odemeler;
        
        if (durum != null && !durum.isEmpty()) {
            odemeler = odemeHizmet.durumIleGetir(durum);
            model.addAttribute("durum", durum);
        } else {
            odemeler = odemeHizmet.tumOdemeleriGetir();
        }
        
        model.addAttribute("odemeler", odemeler);
        return "odemeler";
    }

    @GetMapping("/{no}")
    public String odemeDetayiGoster(@PathVariable Integer no, Model model) {
        Optional<Odeme> odeme = odemeHizmet.odemeGetir(no);
        
        if (odeme.isPresent()) {
            model.addAttribute("odeme", odeme.get());
            
            List<CezaOdemesi> cezaOdemeleri = odemeHizmet.cezaOdemeleriniGetir(no);
            model.addAttribute("cezaOdemeleri", cezaOdemeleri);
            
            return "odeme-detay";
        } else {
            return "redirect:/odemeler";
        }
    }

    @PostMapping("/iptal/{no}")
    public String odemeIptalEt(@PathVariable Integer no, RedirectAttributes redirectAttributes) {
        try {
            boolean sonuc = odemeHizmet.odemeIptalEt(no);
            if (sonuc) {
                redirectAttributes.addFlashAttribute("mesaj", "Ödeme başarıyla iptal edildi.");
            } else {
                redirectAttributes.addFlashAttribute("hata", "Ödeme bulunamadı veya iptali mümkün değil.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hata", "İptal işlemi sırasında hata oluştu: " + e.getMessage());
        }
        return "redirect:/odemeler/" + no;
    }

    @GetMapping("/odenmemis-cezalar")
    public String odenmemisCezalariGoster(Model model) {
        List<CezaOdemesi> odenmemisCezalar = odemeHizmet.odenmemisCezalariGetir();
        model.addAttribute("odenmemisCezalar", odenmemisCezalar);
        return "odenmemis-cezalar";
    }
}
