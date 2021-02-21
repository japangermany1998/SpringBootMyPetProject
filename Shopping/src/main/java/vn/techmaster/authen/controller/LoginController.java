package vn.techmaster.authen.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.techmaster.authen.model.Event;
import vn.techmaster.authen.model.Role;
import vn.techmaster.authen.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import vn.techmaster.authen.security.CookieManager;
import vn.techmaster.authen.service.AuthenException;
import vn.techmaster.authen.service.IAuthenSerVice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes({"User","adminhashpass","Roles"})
public class LoginController {
    @Autowired
    private IAuthenSerVice authenSerVice;

    @Autowired
    private CookieManager manager;

    @GetMapping("/")
    public String enter(Model model,HttpServletRequest request,HttpServletResponse response) throws AuthenException {
        if(!authenSerVice.getPassword().contains("root")){
            authenSerVice.getPassword().add("root");
        }
        if(!authenSerVice.findUserByEmail("admin@techmaster.vn").getRoles().contains("admin")){
            List<String> list=new ArrayList<>();
            list.add("admin");
            authenSerVice.updateRole(1,list);
        }

        if(manager.getAuthenticatedEmail(request)!=null){
            if(manager.getAuthenticatedEmail(request).equals("admin@techmaster.vn")) {
                model.addAttribute("UserLogin", authenSerVice.findUserByEmail("admin@techmaster.vn"));
                return "LoginSuccess";
            }else {
                manager.setNotAuthenticated(response);
                manager.setAuthenticated(response,"admin@techmaster.vn");
                model.addAttribute("UserLogin", authenSerVice.findUserByEmail("admin@techmaster.vn"));
                return "LoginSuccess";
            }
        }else {
            manager.setAuthenticated(response,"admin@techmaster.vn");
            model.addAttribute("UserLogin", authenSerVice.findUserByEmail("admin@techmaster.vn"));
            return "LoginSuccess";
        }
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("User",new User());
        return "login";
    }

    @GetMapping("/update/{id}")
    public String updateAccount(@PathVariable long id,Model model){
        User user= authenSerVice.findUserById(id);
        model.addAttribute("user",user);
        return "UpdateAccount";
    }

    @PostMapping("/update")
    public String updateSuccess(@ModelAttribute User user,BindingResult result,Model model,HttpServletRequest request
    ,HttpServletResponse response) throws AuthenException {
        String loginEmail = manager.getAuthenticatedEmail(request);
        if (loginEmail != null) {
        if(result.hasErrors()){
            model.addAttribute("UserLogin",authenSerVice.findUserByEmail(loginEmail));
            return "redirect:/login/"+authenSerVice.findUserByEmail(loginEmail).getId();
        }
            authenSerVice.updateAccount(user.getId(),user.getFullname(),user.getEmail(),user.getPassword());
            manager.setNotAuthenticated(response);
            manager.setAuthenticated(response,authenSerVice.findUserById(user.getId()).getEmail());
            model.addAttribute("UserLogin",authenSerVice.findUserById(user.getId()));
            return "redirect:/login/"+user.getId();
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
            manager.setNotAuthenticated(response);
            return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginSuccessOrFail(@ModelAttribute User user, BindingResult result,Model model,HttpServletResponse response) {
        if(result.hasErrors()){
            return "login";
        }
        if(!authenSerVice.login(user.getEmail(),user.getPassword())){
            return "login";
        }
            manager.setAuthenticated(response,user.getEmail());
        return "redirect:/login/"+authenSerVice.findUserByEmail(user.getEmail()).getId();
    }

    @GetMapping("/login/{id}")
    public String LoginSuccess(@PathVariable long id,Model model){
        User user=authenSerVice.findUserById(id);
        model.addAttribute("UserLogin",user);

        return "LoginSuccess";
    }

    @GetMapping("/authorization/{hashpass}")
    public String adminauthor(@PathVariable String hashpass, Model model){
        User user=authenSerVice.findUserByEmail("admin@techmaster.vn");
        boolean isAdmin=authenSerVice.isAdmin(user.getId());
        if(!isAdmin){
            model.addAttribute("display","Ban khong co quyen truy cap");
        }
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("Users",authenSerVice.getAll());
        model.addAttribute("adminhashpass",hashpass);
        return "ListUser";
    }

    @GetMapping("/listUser/{id}")
    public String userinfo(@PathVariable long id,Model model){
        User user=authenSerVice.findUserById(id);
        model.addAttribute("user",user);
        model.addAttribute("status","none");
        model.addAttribute("Roles",authenSerVice.getAllRoles());
        return "UserInfo";
    }

    @GetMapping("/deleteRole/{id}/{role}")
    public String deleteRole(@PathVariable long id, @PathVariable String role, Model model
    ,HttpServletRequest request) throws AuthenException {
        User user=authenSerVice.findUserById(id);
        user.removeRole(authenSerVice.findRolebyName(role));
        authenSerVice.updateRole(id,user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        authenSerVice.addEvent(user.getId(),authenSerVice.findUserByEmail(manager.getAuthenticatedEmail(request)).getFullname()+
                " đã loại bỏ vị trí "+role+ " của bạn");
        authenSerVice.addEvent(authenSerVice.findUserByEmail(manager.getAuthenticatedEmail(request)).getId(),
                "Bạn đã loại bỏ vị trí "+role+ " của "+user.getFullname());
        model.addAttribute("user",user);
        model.addAttribute("status","none");
        return "UserInfo";
    }

    @GetMapping("/addRole/{id}")
    public String addRole(@PathVariable long id,Model model) {
        User user=authenSerVice.findUserById(id);
        model.addAttribute("user",user);
        model.addAttribute("status","add");
        model.addAttribute("roleobject",new Role());
        return "UserInfo";
    }

    @PostMapping("/addRole/{id}")
    public String AddRole(@ModelAttribute Role role,@PathVariable long id,BindingResult result,RedirectAttributes redirect,Model model
    ,HttpServletRequest request) throws AuthenException {
        List<String> roles=new ArrayList<>();
        User user=authenSerVice.findUserById(id);
        for (int i=0;i<authenSerVice.getRoles(user.getId()).size();i++){
            roles.add(authenSerVice.getRoles(user.getId()).get(i));
        }
        roles.add(role.getName());
        authenSerVice.updateRole(user.getId(),roles);
        authenSerVice.addEvent(user.getId(),authenSerVice.findUserByEmail(manager.getAuthenticatedEmail(request)).getFullname()+
                " đã thêm vị trí "+role.getName()+ " cho bạn");
        authenSerVice.addEvent(authenSerVice.findUserByEmail(manager.getAuthenticatedEmail(request)).getId(),
                "Bạn đã thêm vị trí "+role.getName()+ " cho "+user.getFullname());
        return "redirect:/listUser/"+user.getId();
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute User user, BindingResult result, RedirectAttributes redirect,
                                HttpServletRequest request, HttpServletResponse response,Model model) {
        if(result.hasErrors()){
            return "CreateAccount";
        }
        if(authenSerVice.getAll().stream().filter(u->u.getEmail().equals(user.getEmail())).count()==0){
            System.out.println(user.getPassword());
            user.setHashPassword(authenSerVice.hashPassword(user.getPassword()));
            authenSerVice.createAccount(user);
            authenSerVice.getPassword().add(user.getPassword());
        }else {
            model.addAttribute("display","email da ton tai");
            return "CreateAccount";
        }
        return "redirect:/login";
    }

    @GetMapping("/forgetpass")
    public String getNewPass(Model model){
        boolean checkMail=false;
        model.addAttribute("checkMail",checkMail);
        return "Newpassword";
    }

    @PostMapping("/addpass")
    public String addEmail(@ModelAttribute User user,Model model){
        if(authenSerVice.findUserByEmail(user.getEmail())!=null){
            model.addAttribute("checkMail",true);
            User user1= authenSerVice.findUserByEmail(user.getEmail());
            model.addAttribute("User1",user1);
        }else {
            model.addAttribute("checkMail",false);
        }
        return "Newpassword";
    }

    @PostMapping("/newpass")
    public String newPass(@ModelAttribute("User1") User user,BindingResult result,Model model) throws AuthenException {
        if(result.hasErrors()){
            model.addAttribute("checkMail",true);
            return "Newpassword";
        }
        System.out.println(user.getId());
        authenSerVice.updateAccount(user.getId(), user.getFullname(),user.getEmail(),user.getPassword());
        return "redirect:/login";
    }

    @GetMapping("/create")
    public String create(Model model){
        return "CreateAccount";
    }
}
