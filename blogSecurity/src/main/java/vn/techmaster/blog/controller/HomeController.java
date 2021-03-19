package vn.techmaster.blog.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.DTO.UserMapper;
//import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.controller.request.RegisterRequest;
import vn.techmaster.blog.controller.request.RoleRequest;
import vn.techmaster.blog.model.Post;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.security.CustomUserDetails;
import vn.techmaster.blog.security.MyUserDetailService;
import vn.techmaster.blog.service.AuthenException;
import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IPostService;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Controller
@SessionAttributes({"roleEditor","roleAdmin","roleAuthor","roleUsers"})
public class HomeController {
  @Autowired private IAuthenService authenService;
  @Autowired private IPostService postService;
  @Autowired private MyUserDetailService userDetailService;
  @Autowired private AuthenticationManager authenticationManager;

  public static final String LOGIN_REQUEST = "loginRequest";
  

  @GetMapping(value = {"/", "/{page}"})
  public String home(@PathVariable(value="page", required = false) Integer page, Model model, Principal principal) {
    if (principal != null) {
      String loginname = principal.getName();
      CustomUserDetails userDetails=(CustomUserDetails)userDetailService.loadUserByUsername(loginname);
      UserInfo user= UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
      model.addAttribute("user", user);
    }
    if (page == null) {
      page = 0;
    }
    Page<Post> pagePosts = postService.findAllPaging(page, 8);

    List<Post> posts = pagePosts.getContent();
    model.addAttribute("posts", posts);
    List<Paging> pagings = Paging.generatePages(page, pagePosts.getTotalPages());
    model.addAttribute("pagings", pagings);
    return Route.HOME;
  }

  @PostMapping("/updateRole")
  public String updateRole(@ModelAttribute RoleRequest roleRequest, BindingResult result, Model model, Principal principal) {
    if(!result.hasErrors()) {
      if (principal != null) {
        String loginname = principal.getName();
        CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(loginname);
        UserInfo user = UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
        System.out.println(roleRequest.getId());
        model.addAttribute("user", user);
        authenService.updateRole(roleRequest.getRoles(), roleRequest.getId());
        return Route.REDIRECT_POSTS;
      }
      return Route.REDIRECT_HOME;
    }
    return Route.REDIRECT_HOME;
  }

  @GetMapping("/login")
  public String showLoginForm(Model model, HttpServletRequest request) {
    return Route.LOGIN_TEMPLATE;
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response)
  {
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();

    if (session != null) {
      session.invalidate();
    }
    for (Cookie cookie : request.getCookies()) {
      cookie.setMaxAge(0);
    }
    return Route.REDIRECT_HOME;
  }

  @GetMapping("/register")
  public String registerForm(Model model){
    RegisterRequest registerRequest=new RegisterRequest();
    model.addAttribute("userRegister",registerRequest);
    return "register";
  }

  @PostMapping("/register")
  public String registerSubmit(@ModelAttribute RegisterRequest req,Model model,BindingResult result,HttpServletRequest request){
    if(!result.hasErrors()){
      User user=authenService.register(req);

      try {
        request.login(req.getEmail(), req.getPassword());
      } catch (ServletException e) {
        LOGGER.error("Error while login ", e);
      }
      return Route.REDIRECT_POSTS;
    }
    return "/register";
  }

  @GetMapping("/admin")
  public String adminPage(Model model,Principal principal){
    if (principal != null) {
      model.addAttribute("users",authenService.getAll());
      String loginname = principal.getName();
      CustomUserDetails userDetails=(CustomUserDetails)userDetailService.loadUserByUsername(loginname);
      UserInfo user= UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
      model.addAttribute("user", user);
      return "admin";
    }
    return Route.REDIRECT_HOME;
  }

  @GetMapping("/user/{id}")
  public String userInfomation(@PathVariable long id,Model model,Principal principal){
    if(principal!=null){
      String loginname = principal.getName();
      CustomUserDetails userDetails=(CustomUserDetails)userDetailService.loadUserByUsername(loginname);
      UserInfo user= UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
      RoleRequest roleRequest=new RoleRequest();
      System.out.println(id);
      roleRequest.setRoles(authenService.findUserById(id).getRoles());
      model.addAttribute("user", user);
      model.addAttribute("roleReq",roleRequest);
      model.addAttribute("userId",id);
      model.addAttribute("roles",authenService.getAllRoles());
      return "UserInfo";
    }
    return Route.REDIRECT_HOME;
  }
}
