package vn.techmaster.blog;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.blog.model.Post;

import javax.persistence.Table;

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
