package vn.techmaster.blog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import vn.techmaster.blog.service.IAuthenService;

@Component
public class CookieManager {
  private final String LOGIN_COOKIE = "loginsuccess";
  @Autowired
  IAuthenService authenService;

  public String getAuthenticatedEmail(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (var cookie:cookies) {
        if (cookie.getName().equals(LOGIN_COOKIE)) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  public void setAuthenticated(HttpServletResponse response, String email, Model model){
    Cookie loginCookie = new Cookie(LOGIN_COOKIE,email);
    loginCookie.setMaxAge(30 * 60);
    loginCookie.setPath("/");
    response.addCookie(loginCookie);
    model.addAttribute("user",authenService.findUserbyMail(email));
  }

  public void setNotAuthenticated(HttpServletResponse response) {
    Cookie loginCookie = new Cookie(LOGIN_COOKIE, null);
    loginCookie.setMaxAge(0);
    loginCookie.setHttpOnly(true);
    loginCookie.setPath("/");
    //add cookie to response
    response.addCookie(loginCookie);
  }
}
