package vn.techmaster.blog.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import vn.techmaster.blog.DTO.PostMapper;
import vn.techmaster.blog.DTO.PostPOJO;
import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.DTO.UserMapper;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.controller.request.IdRequest;
import vn.techmaster.blog.controller.request.PostRequest;
import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.model.Post;
import vn.techmaster.blog.model.Tag;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.repository.RoleRepository;
import vn.techmaster.blog.security.CustomUserDetails;
import vn.techmaster.blog.security.MyUserDetailService;
import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IPostService;
import vn.techmaster.blog.service.PostException;


@Controller
@SessionAttributes({"roleEditor", "roleAdmin", "roleAuthor", "roleUsers"})
public class PostController {
    @Autowired
    private IAuthenService authenService;
    @Autowired
    private IPostService postService;
    @Autowired
    private MyUserDetailService userDetailService;

    @GetMapping("/posts")  //Liệt kê các post của một blogger cụ thể
    public String getAllPosts(Model model, Principal principal) {
        if (principal != null) {
            String loginname = principal.getName();
            CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(loginname);
            User userDetail = userDetails.getUser();
            UserInfo user = UserMapper.INSTANCE.userToUserInfo(userDetail);
            user.setRoles(userDetail.getRoles());
            model.addAttribute("roleEditor", authenService.getRole("EDITOR"));
            model.addAttribute("roleAdmin", authenService.getRole("ADMIN"));
            model.addAttribute("roleAuthor", authenService.getRole("AUTHOR"));

            model.addAttribute("user", user);
            model.addAttribute("roleUsers", user.getRoles());

            List<Post> posts = postService.getAllPostsByUserID(user.getId());
            model.addAttribute("posts", posts);
            return Route.ALLPOSTS;
        } else {
            return Route.REDIRECT_HOME;
        }
    }

    @GetMapping("/post")  //Show form để tạo mới Post
    public String createEditPostForm(Model model, Principal principal) {
        if (principal != null) {
            String loginname = principal.getName();
            CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(loginname);
            UserInfo user = UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
            PostRequest postReqest = new PostRequest();
            postReqest.setUser_id(user.getId());
            model.addAttribute("post", postReqest);
            model.addAttribute("user", user);

            List<Tag> tags = postService.getAllTags();
            model.addAttribute("allTags", tags);
            return Route.POST;
        } else {
            return Route.REDIRECT_HOME;
        }
    }

    @PostMapping("/post")
    public String createEditPostSubmit(@Valid @ModelAttribute("post") PostRequest postRequest, BindingResult bindingResult, Model model, Principal principal) {

        if (bindingResult.hasErrors()) {
            List<Tag> tags = postService.getAllTags();
            model.addAttribute("tags", tags);
            return Route.POST;
        }
        if (principal != null) {
            String loginname = principal.getName();
            CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(loginname);
            UserInfo user = UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
            try {
                if (postRequest.getId() == null) {
                    postService.createNewPost(postRequest); //Create
                } else {
                    postService.updatePost(postRequest);  //Edit
                }
            } catch (PostException pe) {
                return Route.REDIRECT_HOME;
            }

            return Route.REDIRECT_POSTS;
        }
        return Route.REDIRECT_HOME;
    }

    //Lấy ra một post cụ thể cùng comment
    @GetMapping("/post/{id}")
    public String showPostAndComment(@PathVariable("id") long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            PostPOJO postPOJO = PostMapper.INSTANCE.postToPostPOJO(post);
            model.addAttribute("post", postPOJO);

            Set<Tag> tags = post.getTags();
            model.addAttribute("tags", tags);

            List<Comment> comments = post.getComments();
            Collections.reverse(comments);
            model.addAttribute("comments", comments); //Trả  về danh sách comment

            if (principal != null) {//Nếu user login và xem post, cần bổ xung chức năng comment
                String loginname = principal.getName();
                CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(loginname);
                UserInfo user = UserMapper.INSTANCE.userToUserInfo(userDetails.getUser());
                model.addAttribute("user", user); //Người dùng đang login
                model.addAttribute("commentRequest", new CommentRequest(postPOJO.getId()));
            } else {
                model.addAttribute("commentRequest", new CommentRequest());
            }

            return Route.POST_COMMENT;
        } else {
            return Route.REDIRECT_HOME;
        }
    }

    //Xoá một post
    @PostMapping("/post/delete")
    public String deletePost(@ModelAttribute IdRequest idRequest, Principal principal) {
        if (principal != null) {
            postService.deletePostById(idRequest.getId());
        }
        return Route.REDIRECT_POSTS;
    }

    //Mở form để edit một post
    @PostMapping("/post/edit")
    public String editPost(@ModelAttribute IdRequest idRequest, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.findById(idRequest.getId());

        if (principal != null && optionalPost.isPresent()) {
            Post post = optionalPost.get();
            PostRequest postReqest = PostMapper.INSTANCE.postToPostRequest(post);

            model.addAttribute("post", postReqest);
            List<Tag> tags = postService.getAllTags();
            model.addAttribute("allTags", tags);
            UserInfo userInfo = UserMapper.INSTANCE.userToUserInfo(post.getUser());
            model.addAttribute("user", userInfo);
            return Route.POST;
        } else {
            return Route.REDIRECT_POSTS;
        }
    }
}