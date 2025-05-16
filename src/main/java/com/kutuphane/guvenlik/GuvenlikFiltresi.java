package com.kutuphane.guvenlik;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Tüm istekleri yakalayıp güvenlik kontrolü yapan filtre.
 * Giriş yapmamış kullanıcıları login sayfasına yönlendirir.
 */
@Component
public class GuvenlikFiltresi implements Filter {

    @Autowired
    private GuvenlikKonfigurasyon guvenlikKonfigurasyon;
    
    // Güvenlik kontrolünden muaf olan URL'ler
    private static final List<String> GUVENLIK_HARICI_URLS = Arrays.asList(
        "/giris", 
        "/css/", 
        "/js/", 
        "/img/", 
        "/h2-console"
    );
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String yol = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        boolean hariciUrl = GUVENLIK_HARICI_URLS.stream().anyMatch(yol::startsWith);
        
        // Eğer bu kütüphane işlemlerine (korunan sayfalara) ait bir istek ise ve giriş yapılmamışsa (boolean = 0)
        if (!hariciUrl && !guvenlikKonfigurasyon.girisYapildiMi()) {
            // Giriş sayfasına yönlendir
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/giris");
            return;
        }
        
        // İsteği normal işleme devam ettir
        chain.doFilter(request, response);
    }
}
