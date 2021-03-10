package vn.techmaster.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.model.*;
import vn.techmaster.blog.repository.BugRepository;
import vn.techmaster.blog.repository.CommentRepository;
import vn.techmaster.blog.repository.ImagesRepository;
import vn.techmaster.blog.repository.UserRepository;
import vn.techmaster.blog.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BugService implements IBugService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BugRepository bugRepository;

    @Autowired
    ImagesRepository imagesRepository;


    @Override
    public Long addBugRequest(Bug bug, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.addBug(bug);
            bug.setStatus("Not Fixed");
            userRepository.save(user);
            return user.getBugs().get(user.getBugs().size() - 1).getId();
        }
        return null;
    }

    @Override
    public void editBugRequest(String title, String content, long id, MultipartFile[] multipartFile) throws IOException {
        Optional<Bug> optionalBug = bugRepository.findById(id);
        if (optionalBug.isPresent()) {
            Bug bug = optionalBug.get();
            bug.setTitle(title);
            bug.setContent(content);
            if (multipartFile[0].getSize() > 0) {
                List<Images> list = new ArrayList<>();
                bug.setImages(list);
            }
            for (MultipartFile file : multipartFile) {
                if (file.getSize() > 0) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    addImage(fileName, id);
                    FileUtils.saveFile(file, fileName);
                }
            }
            bugRepository.flush();
        }
    }

    @Override
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @Override
    public List<Bug> getUserBugs(String loginEmail) {
        Optional<User> userOptional = userRepository.findByEmail(loginEmail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getBugs();
        }
        return null;
    }

    @Override
    public Optional<Bug> findById(Long id) {
        return bugRepository.findById(id);
    }

    @Override
    public void setBugStatus(long id, String status) {
        Optional<Bug> optionalBug = bugRepository.findById(id);
        if (optionalBug.isPresent()) {
            Bug bug = optionalBug.get();
            bug.setStatus(status);
            bugRepository.flush();
        }
    }

    @Override
    public void deleteBugById(Long id) {
        Bug bug = bugRepository.findById(id).get();
        bugRepository.deleteById(id);
    }

    @Override
    public Comment addComment(CommentRequest commentRequest, long user_id) throws PostException {
        Optional<Bug> oBug = bugRepository.findById(commentRequest.getPost_id());
        Optional<User> oUser = userRepository.findById(user_id);
        if (oBug.isPresent() && oUser.isPresent()) {
            Bug bug = oBug.get();
            Comment comment = new Comment(commentRequest.getContent());
            comment.setUser(oUser.get());
            bug.addComment(comment);
            bugRepository.flush();
            return comment;
        } else {
            throw new PostException("Post or User is missing");
        }
    }

    @Override
    public List<Comment> getAllPostComment(String id) throws PostException {
        Optional<Bug> bugOptional = bugRepository.findById(Long.valueOf(id));
        if (bugOptional.isPresent()) {
            return bugOptional.get().getComments();
        }
        throw new PostException("Not find Post");
    }

    @Override
    public void addImage(String filename, long id) {
        Optional<Bug> oBug = bugRepository.findById(id);
        if (oBug.isPresent()) {
            Bug bug = oBug.get();
            Images images = new Images();
            images.setProPicPath(filename);
            bug.addImage(images);
            bugRepository.flush();
        }
    }


}
