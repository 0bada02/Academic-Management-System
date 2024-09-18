package ju.repository;

import ju.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for Department entity
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);
}