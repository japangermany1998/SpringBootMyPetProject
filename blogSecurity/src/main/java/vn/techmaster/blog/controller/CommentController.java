package vn.techmaster.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.DTO.UserMapper;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.security.CustomUserDetails;
import vn.techmaster.blog.security.MyUserDetailService;
import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IPostService;
import vn.techmaster.blog.service.PostException;

import java.security.Principal;

@Controller
public class CommentController {
  @Autowired
  private IAuthenService authenService;
  @Autowired
  private IPostService postService;
  @Autowired
  private MyUserDetailService userDetailService;

  @PostMapping("/comment")
  public String handlePostComment(@ModelAttribute CommentRequest commentRequest, Principal principal) {
      if (principal != null) {
          String loginname = principal.getName();
          CustomUserDetails userDetails=(CustomUserDetails)userDetailService.loadUserByUsername(loginname);
          UserInfo user= UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
      try {
        postService.addComment(commentRequest, user.getId());
      } catch (PostException e) {
        e.printStackTrace();
      }

      return "redirect:/post/" + commentRequest.getPost_id();

    } else {
      return Route.HOME;
    }
  }

}
