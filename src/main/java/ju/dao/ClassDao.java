package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.Class.Class;
import ju.model.Class.Enum.DaysOfWeek.DaysOfWeek;
import ju.model.Class.Enum.Semester;
import ju.model.Class.Enum.ClassStatus;
import lombok.*;

import java.sql.Time;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDao {
    private Integer id;
    private String courseTitle;
    private Integer courseCreditHours;
    private Time startTime;
    private Time endTime;
    private List<DaysOfWeek> daysOfWeek;
    private String roomNumber;
    private Integer capacity;
    private Semester semester;
    private Year year;
    private ClassStatus status;
    private String instructorName;
    private List<ClassStudentDao> classStudent;
    private String departmentName;

    public ClassDao(Integer id, Time startTime,
                    Time endTime, List<DaysOfWeek> daysOfWeeks,
                    String roomNumber, Integer capacity,
                    Semester semester, Year year,
                    ClassStatus status) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeeks;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.semester = semester;
        this.year = year;
        this.status = status;
    }

    public ClassDao(Integer id, Time startTime,
                    Time endTime, List<DaysOfWeek> daysOfWeeks,
                    String roomNumber, Semester semester, Year year) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeeks;
        this.roomNumber = roomNumber;
        this.semester = semester;
        this.year = year;
    }

    public ClassDao(Class aClass) {
        this.id = aClass.getId();
        this.startTime = aClass.getStartTime();
        this.endTime = aClass.getEndTime();
        this.daysOfWeek = aClass.getDaysOfWeek();
        this.roomNumber = aClass.getRoomNumber();
        this.capacity = aClass.getCapacity();
        this.semester = aClass.getSemester();
        this.year = aClass.getYear();
        this.status = aClass.getStatus();

        if (aClass.getCourse() != null) {
            this.courseTitle = aClass.getCourse().getTitle();
            this.courseCreditHours = aClass.getCourse().getCreditHours();
        }

        if (aClass.getInstructor() != null) {
            this.instructorName = aClass.getInstructor().getName();
        }

        if (aClass.getClassStudents() != null) {
            this.classStudent = aClass.getClassStudents().stream()
                    .map(ClassStudentDao::new)
                    .collect(Collectors.toList());
        }

        if (aClass.getCourse() != null && aClass.getCourse().getDepartment() != null) {
            this.departmentName = aClass.getCourse().getDepartment().getName();
        }
    }
}
