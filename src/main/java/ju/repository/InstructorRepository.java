package ju.repository;

import ju.model.Class.Enum.Semester;
import ju.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.*;

// Repository interface for Instructor entity
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    List<Instructor> findByDepartmentId(Integer departmentId);

    Optional<Instructor> findByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByIdAndDepartmentId(Integer id, Integer departmentId);

    boolean existsByNameOrPhoneOrEmail(String name, String phone, String email);

//    @Query("SELECT i FROM Instructor i " +
//            "JOIN i.classes c " +
//            "JOIN c.course course " +
//            "WHERE i.id = :instructorId " +
//            "AND c.year = :year " +
//            "AND c.semester = :semester")
//    Optional<Instructor> findInstructorWithCoursesByYearAndSemester(
//            @Param("instructorId") Integer instructorId,
//            @Param("year") Year year,
//            @Param("semester") Semester semester);
}
