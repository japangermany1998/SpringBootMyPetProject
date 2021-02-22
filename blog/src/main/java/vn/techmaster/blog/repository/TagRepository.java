package vn.techmaster.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.blog.model.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String name);
}
