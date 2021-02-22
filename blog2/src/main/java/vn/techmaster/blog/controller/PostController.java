package vn.techmaster.blog.controller;

        import javax.servlet.http.HttpServletRequest;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.BindingResult;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;
        import vn.techmaster.blog.controller.search.SearchByKeywordAndTag;
        import vn.techmaster.blog.model.Comment;
        import vn.techmaster.blog.model.Post;
        import vn.techmaster.blog.model.Role;
        import vn.techmaster.blog.model.Tag;
        import vn.techmaster.blog.repository.CommentRepository;
        import vn.techmaster.blog.security.CookieManager;
        import vn.techmaster.blog.service.AuthenException;
        import vn.techmaster.blog.service.IAuthenService;


@Controller
@SessionAttributes({"posts","Tags","user"})
public class PostController {
  @Autowired private CookieManager cookieManager;

  @Autowired private IAuthenService authenService;

  @Autowired private CommentRepository repository;

  @GetMapping("/posts")
  public String getPosts(HttpServletRequest request, Model model) {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      model.addAttribute("user",authenService.findUserbyMail(loginEmail));
      return Route.ALLPOSTS;
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/layout")
  public String writePost(HttpServletRequest request,Model model){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      model.addAttribute("post",new Post());
      return "layout";
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @PostMapping("/write")
  public String writecontent(@ModelAttribute Post post,BindingResult result, HttpServletRequest request, Model model) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      if(post.getId()==null) {
        authenService.addPostUser(loginEmail, post);
      }else {
        authenService.editPost(post.getId(), post.getTitle(),post.getContent(),post.getTags());
      }
      return Route.REDIRECT_POSTS;
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/posts/{id}")
  public String showContent(@PathVariable long id,HttpServletRequest request, Model model){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    model.addAttribute("post",authenService.findPostId(id));
    model.addAttribute("Comment",new Comment());
    return "PostContent";
  }

  @GetMapping("/delete/{id}")
  public String deletePost(@PathVariable long id,HttpServletRequest request,Model model) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      authenService.deletePost(id,loginEmail);
      return Route.REDIRECT_POSTS;
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @PostMapping("/postcomment/{id}")
  public String postComment(@ModelAttribute Comment comment, @PathVariable long id,HttpServletRequest request) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      authenService.addPostComment(id,comment.getContent(),loginEmail);
      return Route.REDIRECT_POSTS+"/"+authenService.findPostId(id).getId();
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/edit/{id}")
  public String editPost(@PathVariable long id,Model model,HttpServletRequest request){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      model.addAttribute("post",authenService.findPostId(id));
      return "layout";
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/delete/{commentId}/{postId}")
  public String deleteComment(@PathVariable long commentId,@PathVariable long postId, Model model,HttpServletRequest request) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      authenService.deleteComment(commentId,loginEmail);
      model.addAttribute("post",authenService.findPostId(postId));
      model.addAttribute("Comment",new Comment());
      return "PostContent";
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/allpost")
  public String getAllPost(Model model){
    model.addAttribute("Posts",authenService.getAllPosts());
    model.addAttribute("searchByKeyTag",new SearchByKeywordAndTag());
    return "AllPosts";
  }

  @PostMapping("/search")
  public String searchPost(@ModelAttribute SearchByKeywordAndTag search, BindingResult result,Model model){
    if(result.hasErrors()){
      return "redirect:/allpost";
    }
    model.addAttribute("Posts",authenService.searchByKeywordAndTag(search.getKeyword(), search.getTag()));
    model.addAttribute("searchByKeyTag",new SearchByKeywordAndTag());
    return "AllPosts";
  }

  @GetMapping("/authorize")
  public String adminauthorize(Model model,HttpServletRequest request){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if (loginEmail != null) {
      model.addAttribute("Users",authenService.getAllUsers());
      model.addAttribute("isAdmin",authenService.isAdmin(loginEmail));
      return "ListUser";
    } else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/listUser/{email}")
  public String userInfo(@PathVariable String email,Model model,HttpServletRequest request){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if(loginEmail!=null){
      if(authenService.isAdmin(loginEmail)){
        model.addAttribute("User",authenService.findUserbyMail(email));
        model.addAttribute("status","none");
        return "UserInfo";
      }
      return "redirect:/posts";
    }else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/addRole/{email}")
  public String addRole(@PathVariable String email,Model model,HttpServletRequest request){
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if(loginEmail!=null){
      if(authenService.isAdmin(loginEmail)){
        model.addAttribute("User",authenService.findUserbyMail(email));
        model.addAttribute("status","add");
        model.addAttribute("Roles",authenService.getAllRoles());
        model.addAttribute("roleobject",new Role());
        return "UserInfo";
      }
      return "redirect:/posts";
    }else {
      return Route.REDIRECT_HOME;
    }
  }

  @PostMapping("/addRole/{email}")
  public String addRoleUser(@ModelAttribute Role role,@PathVariable String email,Model model
          ,HttpServletRequest request) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if(loginEmail!=null){
      if(authenService.isAdmin(loginEmail)){
        model.addAttribute("User",authenService.findUserbyMail(email));
        model.addAttribute("status","none");
        authenService.addRoleUser(role.getName(),email);
        return "redirect:/listUser/"+email;
      }
      return "redirect:/posts";
    }else {
      return Route.REDIRECT_HOME;
    }
  }

  @GetMapping("/deleteRole/{email}/{role}")
  public String deleteRoleUser(@PathVariable String email,@PathVariable String role,
                               HttpServletRequest request, Model model) throws AuthenException {
    String loginEmail = cookieManager.getAuthenticatedEmail(request);
    if(loginEmail!=null){
      if(authenService.isAdmin(loginEmail)){
        model.addAttribute("User",authenService.findUserbyMail(email));
        model.addAttribute("status","none");
        authenService.deleteRoleUser(email,role);
        return "redirect:/listUser/"+email;
      }
      return "redirect:/posts";
    }else {
      return Route.REDIRECT_HOME;
    }
  }
}
