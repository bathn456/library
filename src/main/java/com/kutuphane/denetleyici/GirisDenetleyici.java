package com.kutuphane.denetleyici;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kutuphane.guvenlik.GuvenlikKonfigurasyon;

@Controller
public class GirisDenetleyici {

    @Autowired
    private GuvenlikKonfigurasyon guvenlikKonfigurasyon;

    // Ana sayfa - giriş durumunu model'e ekler
    @RequestMapping("/")
    public String anaSayfa(Model model) {
        // Giriş yapılıp yapılmadığı bilgisini model'e ekle (çıkış butonu göstermek için)
        model.addAttribute("girisYapildi", guvenlikKonfigurasyon.girisYapildiMi());
        return "index";
    }

    @GetMapping("/giris")
    public String giris(@RequestParam(required = false) String hata,
                       @RequestParam(required = false) String cikis,
                       Model model) {
        
        // Kullanıcı zaten giriş yapmışsa anasayfaya yönlendir
        if (guvenlikKonfigurasyon.girisYapildiMi()) {
            return "redirect:/";
        }
        
        if (hata != null) {
            model.addAttribute("hata", "Kullanıcı adı veya şifre hatalı!");
        }
        
        if (cikis != null) {
            model.addAttribute("mesaj", "Başarıyla çıkış yaptınız.");
        }
        
        return "giris";
    }
    
    @PostMapping("/giris")
    public String girisKontrol(@RequestParam String username, 
                              @RequestParam String password,
                              HttpServletRequest request,
                              Model model) {
        
        HttpSession session = request.getSession(true);
        
        if (guvenlikKonfigurasyon.girisYap(username, password, session)) {
            return "redirect:/";
        } else {
            model.addAttribute("hata", "Kullanıcı adı veya şifre hatalı!");
            return "giris";
        }
    }
    
    @GetMapping("/cikis")
    public String cikis(HttpServletRequest request) {
        guvenlikKonfigurasyon.cikisYap(request.getSession(false));
        return "redirect:/giris?cikis";
    }
}
