package vn.techmaster.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.blog.DTO.BugMapper;
import vn.techmaster.blog.DTO.UserInfo;
import vn.techmaster.blog.DTO.UserMapper;
import vn.techmaster.blog.configuration.ConfigListStatus;
import vn.techmaster.blog.controller.request.BugRequest;
import vn.techmaster.blog.controller.request.CommentRequest;
import vn.techmaster.blog.controller.request.IdRequest;
import vn.techmaster.blog.model.Bug;
import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.model.Images;
import vn.techmaster.blog.model.Tag;
import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IBugService;
import vn.techmaster.blog.service.IPostService;
import vn.techmaster.blog.utils.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes({"roleCustomer","roleSupport"})
public class BugController {
    @Autowired
    IAuthenService authenService;

    @Autowired
    IBugService bugService;

    @Autowired
    List<String> list;

    @GetMapping("/bug")
    public String BugAddOrEdit(Model model, HttpServletRequest request){
        UserInfo userInfo=authenService.getLoginedUser(request);
        if(userInfo!=null){
            model.addAttribute("bug",new BugRequest());
            return "Bug";
        }else {
            return Route.REDIRECT_HOME;
        }
    }

    @PostMapping("/uploadImage")
    public @ResponseBody void uploadFile(
            @RequestParam("files") MultipartFile[] multipartFiles
    ) throws IOException {
        FileUtils.multipart=multipartFiles.clone();
    }

    @PostMapping("/bug")
    public String BugAddEdit(@ModelAttribute BugRequest bugRequest, BindingResult result, HttpServletRequest request, Model model
    , @RequestParam("file") MultipartFile[] multipartFile) throws IOException {
        if(result.hasErrors()){
            model.addAttribute("bug",new BugRequest());
            return "Bug";
        }
        UserInfo userInfo=authenService.getLoginedUser(request);
        if(userInfo!=null){
            if(bugRequest.getId() == null){
                Bug bug=new Bug();
                bug.setContent(bugRequest.getContent());
                bug.setTitle(bugRequest.getTitle());
                Long id=bugService.addBugRequest(bug,userInfo.getEmail());
                    for (MultipartFile file : FileUtils.multipart) {
                        System.out.println(file.getOriginalFilename());
                        if(file.getSize()>0) {
                            String fileName=StringUtils.cleanPath(file.getOriginalFilename());
                            System.out.println(fileName);
                            bugService.addImage(fileName, id);
                            FileUtils.saveFile(file, fileName);
                        }
                    }
            }else {
                bugService.editBugRequest(bugRequest.getTitle(),bugRequest.getContent(), bugRequest.getId(),multipartFile);
            }
            return Route.REDIRECT_POSTS;
        }else {
            return Route.REDIRECT_HOME;
        }
    }

    @GetMapping("/showBug")
    public String showAllBugs(HttpServletRequest request,Model model){
        UserInfo userInfo=authenService.getLoginedUser(request);
        if(userInfo!=null){
            model.addAttribute("Bugs",bugService.getAllBugs());
            model.addAttribute("userBugs",bugService.getUserBugs(userInfo.getEmail()));
            model.addAttribute("roleSupport",authenService.getRoleuser("supporter"));
            model.addAttribute("user",userInfo);
            return "AllBugs";
        }else {
            return Route.REDIRECT_HOME;
        }
    }

    @GetMapping("/showBug/{id}")
    public String showBugContent(@PathVariable long id,Model model, HttpServletRequest request){
        UserInfo userInfo=authenService.getLoginedUser(request);
        if(userInfo!=null){
            Optional<Bug> bugOptional=bugService.findById(id);
            if(bugOptional.isPresent()) {
                Bug bug=bugOptional.get();
                if (userInfo.getRoles().contains(authenService.getRoleuser("supporter")) || bug.getUser().getId() == userInfo.getId()) {
                    model.addAttribute("AllStatus", list);
                    model.addAttribute("bug", bug);
                    model.addAttribute("user", userInfo);
//                    List<Comment> comments = bug.getComments();
//                    Collections.reverse(comments);
//                    model.addAttribute("comments", comments);
                    model.addAttribute("commentRequest", new CommentRequest(bug.getId()));
                    return "BugContent";
                }
            }
            return Route.REDIRECT_POSTS;
        }else {
            return Route.REDIRECT_HOME;
        }
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@ModelAttribute Bug bugRequest,Model model, HttpServletRequest request){
        UserInfo userInfo=authenService.getLoginedUser(request);
        if(userInfo!=null){
            bugService.setBugStatus(bugRequest.getId(),bugRequest.getStatus());
            return "redirect:/showBug/"+bugRequest.getId();
        }else {
            return Route.REDIRECT_HOME;
        }
    }

    @PostMapping("/bug/edit")
    public String editBug(@ModelAttribute IdRequest idRequest,HttpServletRequest request,Model model){
        UserInfo user = authenService.getLoginedUser(request);
        Optional<Bug> bugOptional=bugService.findById(idRequest.getId());
        if(user!=null&&bugOptional.isPresent()){
            Bug bug=bugOptional.get();
            BugRequest bugRequest= BugMapper.INSTANCE.bugToBugRequest(bug);
            bugRequest.setImages(bug.getImages());
            model.addAttribute("bug", bugRequest);
            UserInfo userInfo = UserMapper.INSTANCE.userToUserInfo(bug.getUser());
            model.addAttribute("user", userInfo);
            return "Bug";
        }else {
            return Route.REDIRECT_POSTS;
        }
    }

    @PostMapping("/bug/delete")
    public String deleteBug(@ModelAttribute IdRequest idRequest,HttpServletRequest request,Model model){
        UserInfo user = authenService.getLoginedUser(request);
        if (user != null) {
            bugService.deleteBugById(idRequest.getId());
        }
        return Route.REDIRECT_POSTS;
    }

}
