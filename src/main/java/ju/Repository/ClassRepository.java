package ju.Repository;

import ju.Model.Class.Class;
import ju.Model.Class.Enum.DaysOfWeek.DaysOfWeek;
import ju.Model.Class.Enum.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.time.Year;
import java.util.*;

// Repository interface for Class entity
public interface ClassRepository extends JpaRepository<Class, Integer> {
    List<Class> findByCourseId(Integer courseId); // Find classes by course ID

    boolean existsByIdAndCourseId(Integer id, Integer courseId);

    Optional<Class> findByIdAndCourseId(Integer id, Integer courseId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Class c " +
            "WHERE c.year = :year AND c.semester = :semester AND c.roomNumber = :roomNumber " +
            "AND ( " +
            "   (c.startTime < :endTime AND c.endTime > :startTime) " +
            "   OR (c.startTime = :startTime AND c.endTime = :endTime) " +
            ") AND c.daysOfWeek = :daysOfWeek")
    boolean existsByYearAndSemesterAndRoomNumberAndDaysOfWeekAndStartTimeAndEndTimeOrOverlapping(
            @Param("year") Year year,
            @Param("semester") Semester semester,
            @Param("roomNumber") String roomNumber,
            @Param("daysOfWeek") List<DaysOfWeek> daysOfWeek,
            @Param("startTime") Time startTime,
            @Param("endTime") Time endTime
    );
}