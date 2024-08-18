package ju.Repository;

import ju.Model.ClassStudent.ClassStudent;
import ju.Model.ClassStudent.ClassStudentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassStudentRepositry extends JpaRepository<ClassStudent, ClassStudentId> {
}
