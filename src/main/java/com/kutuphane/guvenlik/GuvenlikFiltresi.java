package com.kutuphane.guvenlik;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.stereotype.Component;

@Component
public class GuvenlikFiltresi implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

   HttpServletRequest httpRequest = (HttpServletRequest) request;
   HttpServletResponse httpResponse = (HttpServletResponse) response;
   HttpSession session = httpRequest.getSession(false);

   boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
   boolean isLoginPage = httpRequest.getRequestURI().endsWith("/giris");
   boolean isStaticResource = httpRequest.getRequestURI().startsWith("/css/");
   boolean isPublicPage = httpRequest.getRequestURI().equals("/") || 
                         httpRequest.getRequestURI().equals("/anasayfa") ||
                         httpRequest.getRequestURI().startsWith("/kitaplar") ||
                         httpRequest.getRequestURI().startsWith("/kategoriler");

   if (isLoggedIn || isLoginPage || isStaticResource || isPublicPage) {
     chain.doFilter(request, response);
   } else {
     httpResponse.sendRedirect("/giris");
   }
  }
}