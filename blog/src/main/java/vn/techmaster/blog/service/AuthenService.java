package vn.techmaster.blog.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.techmaster.blog.controller.request.LoginRequest;
import vn.techmaster.blog.model.*;
import vn.techmaster.blog.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenService implements IAuthenService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private TagRepository tagRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void login(LoginRequest loginRequest) throws AuthenException {
    var user = userRepository.findByEmail(loginRequest.getEmail());
    if (user.isPresent()) {
      if (!checkPass(loginRequest.getPassword(),user.get().getPassword())){
        throw new AuthenException("Wrong password");
      }
    } else {
      throw new AuthenException("User with email " + loginRequest.getEmail() + " does not exist");
    }
  }

  @Override
  public User findUserbyMail(String email) {
    if(userRepository.findByEmail(email).isPresent()) {
      return userRepository.findByEmail(email).get();
    }
    return null;
  }

  @Override
  public void addPostUser(String email, Post post) throws AuthenException{
    User user=userRepository.findByEmail(email).get();
    user.addPost(post);
    try {
      userRepository.save(user);
    }catch (Exception ex){
      throw new AuthenException("Loi update");
    }
  }

  @Override
  public void addPostComment(long id, String content,String email) throws AuthenException{
    Post post=postRepository.findById(id).get();
    Comment comment=new Comment();
    comment.setContent(content);
    post.addComment(comment);
    try {
      postRepository.save(post);
    }catch (Exception ex){
      throw new AuthenException("Loi update");
    }
    User user=userRepository.findByEmail(email).get();
    user.addComment(commentRepository.findAll().get(commentRepository.findAll().size()-1));
    try {
      userRepository.save(user);
    }catch (Exception ex){
      throw new AuthenException("Loi update");
    }
  }

  @Override
  public Post findPostId(long id)
  {
    if(postRepository.findById(id).stream().count()!=0) {
      return postRepository.findById(id).get();
    }
    return null;
  }

  @Override
  public void editPost(long id,String title,String content,Set<Tag> tags) throws AuthenException {
    Post post=postRepository.findById(id).get();
    post.setContent(content);
    post.setTitle(title);
//    List<String> tagnames= Arrays.asList(tagname.clone());
//    Set<Tag> tags=tagRepository.findAll().stream().filter(v->tagnames.contains(v.getName())).collect(Collectors.toSet());
    post.setTags(tags);
    try {
      postRepository.save(post);
    }catch (Exception ex){
      throw new AuthenException("Loi update");
    }
  }

  @Override
  public void deleteComment(long id,String email) throws AuthenException {
    Comment comment=commentRepository.findById(id).get();
    User user=userRepository.findByEmail(email).get();
    user.removeComment(comment);
    commentRepository.delete(comment);
    try {
      userRepository.save(user);
    }catch (Exception e){
      throw new AuthenException("Loi");
    }
  }

  @Override
  public List<Post> getAllPosts(){
    return postRepository.findAll();
  }

  @Override
  public List<Post> searchByKeywordAndTag(String keyword, List<Tag> tagname){
    if(tagname.size()==0){
      return postRepository.findAll().stream().filter(v->v.getTitle().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }
    Set<Tag> tags=tagRepository.findAll().stream().filter(v->tagname.contains(v)).collect(Collectors.toSet());
    return postRepository.findAll().stream().filter(v->v.getTitle().toLowerCase().contains(keyword.toLowerCase())&&v.getTags().containsAll(tags)).collect(Collectors.toList());
  }

  @Override
  public List<Tag> getAllTags(){
    return tagRepository.findAll();
  }

  @Override
  public String hashPassword(String plainTextPassword){
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }

  private boolean checkPass(String plainPassword, String hashedPassword) {
    if (BCrypt.checkpw(plainPassword, hashedPassword))
      return true;
    else
      return false;
  }

  @Override
  public List<User> getAllUsers(){
    return userRepository.findAll();
  }

  @Override
  public void saveUser(String id,String hashpassword){
    User user=userRepository.findById(id).get();
    user.setPassword(hashpassword);
    userRepository.save(user);
  }

  @Override
  public void deletePost(long id,String email) throws AuthenException {
    Post post=postRepository.findById(id).get();
    User user=userRepository.findByEmail(email).get();
    user.removePost(post);
    try {
      userRepository.save(user);
      postRepository.delete(post);
    }catch (Exception e){
      throw new AuthenException("Loi");
    }
  }

  @Override
  public void createAccount(String fullname, String email, String password) throws AuthenException {
    if (!userRepository.findByEmail(email).isPresent()) {
      User user = new User(fullname, email);
      user.setPassword(hashPassword(password));
      userRepository.save(user);
    }else {
      throw new AuthenException("Email da ton tai");
    }
  }

  @Override
  public void addRoleUser(String role, String email) throws AuthenException {
    User user=userRepository.findByEmail(email).get();
    Role role1=roleRepository.findByName(role).get();
    user.addRole(role1);
    try {
      userRepository.save(user);
    }catch (Exception e){
      throw new AuthenException("Add role loi");
    }
  }

  @Override
  public boolean isAdmin(String email){
    if(userRepository.findByEmail(email).get().getRoles().size()==0){
      return false;
    }
    if(userRepository.findByEmail(email).get().getRoles().stream()
            .collect(Collectors.toList()).contains(roleRepository.findByName("admin").get())){
      return true;
    }
    return false;
  }

  @Override
  public List<Role> getAllRoles(){
    return roleRepository.findAll();
  }

  @Override
  public void deleteRoleUser(String email, String role) throws AuthenException {
    User user=userRepository.findByEmail(email).get();
    Role role1=roleRepository.findByName(role).get();

    user.removeRole(role1);
    try {
      userRepository.save(user);
    }catch (Exception e){
      throw new AuthenException("Lỗi xóa role");
    }
  }
}
