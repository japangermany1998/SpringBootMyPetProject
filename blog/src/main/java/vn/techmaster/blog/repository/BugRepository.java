package vn.techmaster.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.blog.model.Bug;

public interface BugRepository extends JpaRepository<Bug,Long> {
}
