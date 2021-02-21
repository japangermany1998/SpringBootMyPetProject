package vn.techmaster.authen.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import vn.techmaster.authen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);

}
