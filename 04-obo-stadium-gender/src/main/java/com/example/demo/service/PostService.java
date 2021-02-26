package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService{
    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public void generateSampleData() {
        Lorem lorem = LoremIpsum.getInstance();

        Random random = new Random();
        String[] user={"Cody","Brian","Elena","Peter","Jack","Mary"};
        for (int k = 0; k < 200; k++) {
            Post post = new Post(LocalDateTime.now(),lorem.getTitle(2, 5), lorem.getParagraphs(1, 4),"news-3-thumnails.jpg");
            post.setUser(user[random.nextInt(user.length)]);
            postRepository.save(post);
        }
    }

    @Override
    public Page<Post> findAllPaging(int page, int pageSize) {
        return postRepository.findAll(PageRequest.of(page,pageSize,Sort.by("lastUpdate").descending()));
    }

    @Override
    public Post getPostSlugId(String slug, long id){
        Optional<Post> optionalPost=postRepository.findByIdAndAndSlug(id,slug);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        return null;
    }

    @Override
    public List<Post> getRelatedPost(String user){
        return postRepository.findAllByUser(user,Sort.by("lastUpdate").descending())
                .stream().limit(6).collect(Collectors.toList());
    }
}
