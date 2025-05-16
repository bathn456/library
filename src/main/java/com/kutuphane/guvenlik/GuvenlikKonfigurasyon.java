package com.kutuphane.guvenlik;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

/**
 * Kullanıcı girişi ve oturum yönetimi için kullanılan sınıf
 */
@Component
public class GuvenlikKonfigurasyon {
    
    private static final String OTURUM_KULLANICI = "oturumKullanici";
    
    // Giriş durumunu temsil eden boolean değişken (0: giriş yapılmadı, 1: giriş yapıldı)
    private boolean girisYapildi = false; // varsayılan olarak 0 (false)
    
    // Sabit kullanıcı listesi 
    private static final Map<String, String> KULLANICILAR;
    
    static {
        Map<String, String> kullanicilar = new HashMap<>();
        kullanicilar.put("admin", "admin123");
        KULLANICILAR = Collections.unmodifiableMap(kullanicilar);
    }
    
    /**
     * Kullanıcı giriş işlemini yapar
     * @param kullaniciAdi Kullanıcı adı
     * @param sifre Kullanıcı şifresi
     * @param session HTTP oturumu
     * @return Giriş başarılı ise true, değilse false
     */
    public boolean girisYap(String kullaniciAdi, String sifre, HttpSession session) {
        if (KULLANICILAR.containsKey(kullaniciAdi) && KULLANICILAR.get(kullaniciAdi).equals(sifre)) {
            session.setAttribute(OTURUM_KULLANICI, kullaniciAdi);
            girisYapildi = true; // Giriş durumunu 1 (true) yap
            return true;
        }
        return false;
    }
    
    /**
     * Kullanıcının çıkış işlemini yapar
     * @param session HTTP oturumu
     */
    public void cikisYap(HttpSession session) {
        girisYapildi = false; // Giriş durumunu 0 (false) yap
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Kullanıcının giriş yapıp yapmadığını kontrol eder
     * @return Giriş yapmışsa true (1), yapmamışsa false (0)
     */
    public boolean girisYapildiMi() {
        return girisYapildi;
    }
    
    /**
     * Giriş yapmış kullanıcının adını döndürür
     * @param request HTTP isteği
     * @return Kullanıcı adı veya null
     */
    public String getKullaniciAdi(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(OTURUM_KULLANICI) : null;
    }
}
