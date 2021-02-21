package vn.techmaster.authen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.techmaster.authen.repository.RoleRepository;
import vn.techmaster.authen.repository.UserRepository;
import vn.techmaster.authen.service.IAuthenSerVice;

@SpringBootApplication
public class ShoppingcartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartApplication.class, args);

	}

}
