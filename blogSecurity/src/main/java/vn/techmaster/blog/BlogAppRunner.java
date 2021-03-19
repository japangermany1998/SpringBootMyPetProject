package vn.techmaster.blog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vn.techmaster.blog.service.IAuthenService;
import vn.techmaster.blog.service.IPostService;

@Component
public class BlogAppRunner implements CommandLineRunner {
  @Autowired
  IPostService postService;

  @Autowired
  IAuthenService authenService;

  @Override
  public void run(String... args) throws Exception {
    authenService.generateUsersRoles();
    postService.generateSampleData();
  }
}
