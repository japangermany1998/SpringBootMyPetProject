package vn.techmaster.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.techmaster.blog.model.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images,Long> {
}
