package vn.techmaster.blog.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "comment")
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String content;
  private LocalDateTime lastUpdate;
  @PrePersist //Trước khi lưu khi khởi tạo record
  public void prePersist() {
    lastUpdate = LocalDateTime.now();
  }
  @PreUpdate //Khi cập nhật record
  public void preUpdate() {
    lastUpdate = LocalDateTime.now();
  }

  public Comment(String content) {
    this.content = content;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  private User user; //Mỗi comment phải do một commenter viết

  public void setUser(User user) {
    user.getComments().add(this);
    this.user = user;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  private Post post; //Mỗi comment phải gắn vào một post

  @ManyToOne(fetch = FetchType.EAGER)
  private Bug bug;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public User getUser() {
    return user;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public Bug getBug() {
    return bug;
  }

  public void setBug(Bug bug) {
    this.bug = bug;
  }

  @Override
  public String toString() {
    return "Comment{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", lastUpdate=" + lastUpdate +
            ", user=" + user +
            ", post=" + post +
            ", bug=" + bug +
            '}';
  }
}