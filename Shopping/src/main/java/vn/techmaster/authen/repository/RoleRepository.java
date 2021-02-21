package vn.techmaster.authen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import vn.techmaster.authen.model.Role;

@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
