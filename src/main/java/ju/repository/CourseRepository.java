package ju.repository;

import ju.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

// Repository interface for Course entity
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByDepartmentId(Integer departmentId);

    Optional<Course> findByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByTitle(String title);

    Optional<Course> findByTitle(String title);
}