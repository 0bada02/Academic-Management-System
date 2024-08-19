package ju.Repository;

import ju.Model.ClassStudent.*;
import org.springframework.data.jpa.repository.*;

public interface ClassStudentRepositry extends JpaRepository<ClassStudent, ClassStudentId> {
    boolean existsByStudentId(Integer studentId);
}
