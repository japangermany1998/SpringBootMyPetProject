package vn.techmaster.topcar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.techmaster.topcar.repository.TopCar;

@Configuration
public class RepoConfig {

  @Bean
  public TopCar bookDao() {
    return new TopCar("topcar.csv");
  }
  
}
