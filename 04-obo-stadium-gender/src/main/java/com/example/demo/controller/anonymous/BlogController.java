package com.example.demo.controller.anonymous;

import com.example.demo.entity.Post;
import com.example.demo.model.Paging;
import com.example.demo.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class BlogController {
    @Autowired
    private IPostService postService;

    @GetMapping(value = {"/tin-tuc","/tin-tuc/{page}"})
    public String getNewsPage(@PathVariable(value = "page",required = false) String page, Model model){
        if(page == null){
            page = "1";
        }
        int pageNumber;
        try {
             pageNumber = Integer.valueOf(page);
             if(pageNumber <= 0){
                 return "error/500";
             }
        }catch (NumberFormatException e){
            return "error/500";
        }
        Page<Post> pagePosts = postService.findAllPaging(pageNumber - 1, 8);
        List<Post> posts=pagePosts.getContent();

        model.addAttribute("posts",posts);
        List<Paging> pagings = Paging.generatePages(pageNumber-1, pagePosts.getTotalPages());
        model.addAttribute("pagings",pagings);
        return "shop/news";
    }

    @GetMapping("/tin-tuc/{slug}/{id}")
    public String getNewsContent(@PathVariable String slug,@PathVariable long id,Model model){
        Post post=postService.getPostSlugId(slug,id);
        model.addAttribute("post",post);
        model.addAttribute("Postrelates",postService.getRelatedPost(post.getUser()));
        return "shop/post";
    }
}
