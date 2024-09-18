package ju.repository;

import ju.model.ClassStudent.*;
import org.springframework.data.jpa.repository.*;

import java.util.ArrayList;
import java.util.Optional;

public interface ClassStudentRepositry extends JpaRepository<ClassStudent, ClassStudentId> {
    boolean existsByStudentId(Integer studentId);
    Optional<ArrayList<ClassStudent>> findByaClassId(Integer classId);
}