package ju.Repository;

import ju.Model.Student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

// Repository interface for Student entity
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByDepartmentId(Integer departmentId);

    Optional<Student> findByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByNameOrPhoneOrEmail(String name, String phone, String email);
}