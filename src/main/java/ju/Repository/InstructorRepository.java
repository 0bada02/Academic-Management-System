package ju.Repository;

import ju.Model.Instructor.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

// Repository interface for Instructor entity
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    List<Instructor> findByDepartmentId(Integer departmentId);

    Optional<Instructor> findByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByNameOrPhoneOrEmail(String name, String phone, String email);
}
