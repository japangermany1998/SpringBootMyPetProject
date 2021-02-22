package vn.techmaster.blog.service;

import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.model.*;

import java.util.List;
import java.util.Set;

public interface IAuthenService {
  public void login(LoginRequest loginRequest) throws AuthenException;

  public User findUserbyMail(String email);

  public void addPostUser(String email, Post post) throws AuthenException;

  void addPostComment(long id, String content,String email) throws AuthenException;

  Post findPostId(long id);

  public void editPost(long id, String title, String content, Set<Tag> tags) throws AuthenException;

  public void deleteComment(long id,String email) throws AuthenException;

    List<Post> getAllPosts();

    List<Post> searchByKeywordAndTag(String keyword, List<Tag> tagname);

  List<Tag> getAllTags();

  String hashPassword(String plainTextPassword);

  List<User> getAllUsers();

  void saveUser(String id,String password);

  void deletePost(long id,String email) throws AuthenException;

  void createAccount(String fullname, String email, String password) throws AuthenException;

    void addRoleUser(String role, String email) throws AuthenException;

  boolean isAdmin(String email);

    List<Role> getAllRoles();

  void deleteRoleUser(String email, String role) throws AuthenException;
}
