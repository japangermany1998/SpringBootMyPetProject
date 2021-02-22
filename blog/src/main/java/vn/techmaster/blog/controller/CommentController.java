package vn.techmaster.blog.controller;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import vn.techmaster.blog.DTO.CommentMapper;
import vn.techmaster.blog.DTO.CommentPOJO;
import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IBugService;
import vn.techmaster.blog.service.IPostService;
import vn.techmaster.blog.service.PostException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public class CommentController {
  @Autowired
  private IAuthenService authenService;
  @Autowired
  private IPostService postService;
  @Autowired
  private IBugService bugService;

  ObjectMapper mapper = new ObjectMapper();

//  @PostMapping("/commentPost")
//  public String handlePostComment(@ModelAttribute CommentRequest commentRequest, HttpServletRequest request) {
//    UserInfo userLogin = authenService.getLoginedUser(request);
//    if (userLogin != null) {
//      try {
//        postService.addComment(commentRequest, userLogin.getId());
//      } catch (PostException e) {
//        e.printStackTrace();
//      }
//
//      return "redirect:/post/" + commentRequest.getPost_id();
//
//    } else {
//      return Route.HOME;
//    }
//  }

  @PostMapping("/commentBug")
  public ResponseEntity<?> handleBugComment(@RequestParam("comment") String comment,@RequestParam("Bug_id") String bug_id,
                                  HttpServletRequest request) {
    UserInfo userLogin = authenService.getLoginedUser(request);
    CommentPOJO commentPOJO=new CommentPOJO();
    if (userLogin != null) {
      try {
        CommentRequest commentRequest=new CommentRequest();
        commentRequest.setPost_id(Long.parseLong(bug_id));
        commentRequest.setContent(comment);
        commentPOJO= CommentMapper.INSTANCE.commentToCommentPOJO(bugService.addComment(commentRequest, userLogin.getId()));
      } catch (PostException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Error:"+e.getMessage(),HttpStatus.BAD_REQUEST);
      }
    }
    try {
      return new ResponseEntity<>(mapper.writeValueAsString(commentPOJO),HttpStatus.OK);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>("Error:"+e.getMessage(),HttpStatus.BAD_REQUEST);
    }

  }

  @GetMapping("/commentPost/{id}")
  public List<CommentPOJO> getAllCommentBugs(@PathVariable String id) throws PostException {
    List<CommentPOJO> pojoList=new ArrayList<>();
    bugService.getAllPostComment(id).forEach(v->pojoList.add(CommentMapper.INSTANCE.commentToCommentPOJO(v)));
    return pojoList;
  }

}
