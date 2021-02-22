package vn.techmaster.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.blog.model.Post;
import vn.techmaster.blog.model.User;
import vn.techmaster.blog.repository.PostRepository;
import vn.techmaster.blog.repository.UserRepository;

import javax.persistence.Table;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostRepositoryTest {
//  @Autowired
//    PostRepositoryTest repositoryTest;

  @Test
  public void addComment(){
    List<String> list1=new ArrayList<>();
    list1.add("java");
    list1.add("Spring Boot");
    List<String> list2=new ArrayList<>();
    list2.add("java");
    System.out.println(list1.containsAll(list2));
  }
}