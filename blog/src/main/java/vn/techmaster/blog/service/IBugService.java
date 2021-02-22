package vn.techmaster.blog.service;

import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.model.Bug;
import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.model.Images;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBugService {
    public Long addBugRequest(Bug bug,String email);
    public void editBugRequest(String title, String content, long id, MultipartFile[] multipartFiles) throws IOException;
    List<Bug> getAllBugs();
    List<Bug> getUserBugs(String loginEmail);

    Optional<Bug> findById(Long id);

    void setBugStatus(long id, String status);

    void deleteBugById(Long id);

    Comment addComment(CommentRequest commentRequest, long user_id) throws PostException;

    List<Comment> getAllPostComment(String id) throws PostException;

    void addImage(String filename, long id);
}
