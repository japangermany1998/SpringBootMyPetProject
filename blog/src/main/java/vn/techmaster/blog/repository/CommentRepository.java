package vn.techmaster.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.model.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByPost(Post post);
}
