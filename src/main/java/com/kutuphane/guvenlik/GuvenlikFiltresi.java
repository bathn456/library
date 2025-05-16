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

   if (isLoggedIn || isLoginPage || isStaticResource) {
     chain.doFilter(request, response);
   } else {
     httpResponse.sendRedirect("/giris");
   }
  }
}