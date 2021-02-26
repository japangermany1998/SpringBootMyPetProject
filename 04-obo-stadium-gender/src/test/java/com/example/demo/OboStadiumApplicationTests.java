package com.example.demo;

import com.example.demo.repository.PostRepository;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OboStadiumApplicationTests {
	@Autowired
	PostRepository repository;

	@Test
	void contextLoads() {
		try {
			System.out.println(Integer.valueOf("2"));
		}catch (NumberFormatException e){
			System.out.println(0);
		}

	}

}
