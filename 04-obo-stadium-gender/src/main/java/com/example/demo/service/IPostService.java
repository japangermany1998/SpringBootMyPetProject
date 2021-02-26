package com.example.demo.service;

import com.example.demo.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPostService {
    public List<Post> getAllPosts();
    public void generateSampleData();
    public Page<Post> findAllPaging(int page, int pageSize);

    Post getPostSlugId(String slug, long id);

    List<Post> getRelatedPost(String user);
}
