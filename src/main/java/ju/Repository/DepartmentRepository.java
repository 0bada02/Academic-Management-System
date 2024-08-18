package ju.Repository;

import ju.Model.Department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for Department entity
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);
}