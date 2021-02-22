package vn.techmaster.blog.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Table(name = "users")  //Để không bị lỗi khi kết nối vào Postgresql
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id private long id;

  @Column(nullable = false, length = 64)
  private String fullname;

  @NaturalId
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  //Một User viết nhiều Post
  @OneToMany(
          cascade = CascadeType.ALL,
          orphanRemoval = true,
          fetch = FetchType.LAZY
  )
  @JoinColumn(name = "user_id")
  private List<Post> posts = new ArrayList<>();
  public void addPost(Post post) {
    posts.add(post);
    post.setUser(this);
  }

  public void removePost(Post post) {
    posts.remove(post);
    post.setUser(null);
  }

  @ManyToMany()
  @JoinTable(
          name ="user_role",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles=new HashSet<>();

  public void addRole(Role role){
    roles.add(role);
    role.getUsers().add(this);
  }

  public void removeRole(Role role){
    roles.remove(role);
    role.getUsers().remove(this);
  }

  @OneToMany(cascade = CascadeType.ALL,
          orphanRemoval = true,
          fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private List<Bug> bugs=new ArrayList<>();

  public void addBug(Bug bug){
    bugs.add(bug);
    bug.setUser(this);
  }

  public void removeBug(Bug bug){
    bugs.remove(bug);
    bug.setUser(null);
  }

  //Một User viết nhiều Comment
  @OneToMany(
          cascade = CascadeType.ALL,
          orphanRemoval = true,
          fetch = FetchType.LAZY
  )
  @JoinColumn(name = "user_id")
  private List<Comment> comments = new ArrayList<>();
  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setUser(null);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public List<Bug> getBugs() {
    return bugs;
  }

  public void setBugs(List<Bug> bugs) {
    this.bugs = bugs;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}