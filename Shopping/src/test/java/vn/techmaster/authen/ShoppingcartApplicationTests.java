package vn.techmaster.authen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;
import vn.techmaster.authen.model.Role;
import vn.techmaster.authen.model.User;
import vn.techmaster.authen.repository.RoleRepository;
import vn.techmaster.authen.repository.UserRepository;
import vn.techmaster.authen.service.IAuthenSerVice;
//import vn.techmaster.authen.repository.UserRepository;
//import vn.techmaster.authen.service.AuthService;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ShoppingcartApplicationTests {
	@Autowired
	UserRepository repository;
//	@Autowired
//	IAuthenSerVice serVice;
	@Test
	public void contextLoads() {
		User user=new User();
		user.setFullname("@@@");
		user.setEmail("Dadadasda@da");
		user.setPassword("123444");
		repository.saveAndFlush(user);
		repository.findAll().forEach(System.out::println);

		System.out.println(repository.findAll().stream().filter(u->u.getEmail().equals("mworsam0@state.tx.us")&&u.getPassword().equals("Xwf5wM9bescN")).limit(1).count()!=0);

	}

}
