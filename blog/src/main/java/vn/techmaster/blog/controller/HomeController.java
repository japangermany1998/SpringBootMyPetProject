package vn.techmaster.blog.controller;

import org.dom4j.rule.Mode;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.security.CookieManager;
import vn.techmaster.blog.service.AuthenException;
import vn.techmaster.blog.service.IAuthenService;
@Controller
@SessionAttributes({"Tags","user"})
public class HomeController {
  @Autowired private IAuthenService authenService;

  @Autowired private CookieManager cookieManager;
  public static final String LOGIN_REQUEST = "loginRequest";
  

  @GetMapping("/")
  public String home(Model model)
  {
    model.addAttribute("Tags",authenService.getAllTags());
    return "home.html";
  }

  @GetMapping("/login")
  public String showLoginForm(Model model,HttpServletRequest request,HttpServletResponse response) {
    model.addAttribute("Tags",authenService.getAllTags());
    if (cookieManager.getAuthenticatedEmail(request) != null&&
            authenService.findUserbyMail(cookieManager.getAuthenticatedEmail(request))!=null) {
      return Route.REDIRECT_POSTS;
    }
    if(cookieManager.getAuthenticatedEmail(request) != null) {
      cookieManager.setNotAuthenticated(response);
    }
    model.addAttribute(LOGIN_REQUEST, new LoginRequest());
    return Route.LOGIN_TEMPLATE;
  }

  @GetMapping("/logout")
  public String logout(HttpServletResponse response, HttpServletRequest request, SessionStatus sessionStatus) {
    sessionStatus.setComplete();
    cookieManager.setNotAuthenticated(response);
    return Route.REDIRECT_HOME;
  }

  @PostMapping("/login")
  public String handleLogin(@ModelAttribute LoginRequest loginRequest, 
    BindingResult bindingResult, 
    Model model,
    HttpServletResponse response){      
      if (!bindingResult.hasErrors()) {        
        try {
          authenService.login(loginRequest);
          cookieManager.setAuthenticated(response, loginRequest.getEmail(),model);
          return Route.REDIRECT_POSTS; 
        } catch (AuthenException e) {
          model.addAttribute(LOGIN_REQUEST, new LoginRequest(loginRequest.getEmail(), ""));
          model.addAttribute("errorMessage", e.getMessage());
          return Route.LOGIN_TEMPLATE;
        }
      } else {
        model.addAttribute(LOGIN_REQUEST, new LoginRequest());
        model.addAttribute("errorMessage", "Submitted is invalid");
        return Route.LOGIN_TEMPLATE;
      }
  }

  @GetMapping("/create")
  public String createPage(Model model){
    model.addAttribute("user",new User());
    return "CreateAccount";
  }

  @PostMapping("/create")
  public String createAccount(@ModelAttribute User user,BindingResult result,Model model){
    if(!result.hasErrors()){
      try {
        authenService.createAccount(user.getFullname(),user.getEmail(),user.getPassword());
        return "redirect:/login";
      }catch (AuthenException e){
        model.addAttribute("errorMessage",e.getMessage());
        return "redirect:/create";
      }
    }else {
      model.addAttribute("errorMessage", "Created is invalid");
      return "redirect:/create";
    }
  }
}
