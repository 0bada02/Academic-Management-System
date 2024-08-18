package ju.Model.Class;

import ju.Model.Class.Enum.DaysOfWeek.DaysOfWeek;
import ju.Model.Class.Enum.Semester;
import ju.Model.ClassStudent.ClassStudentDTO;
import ju.Model.Class.Enum.ClassStatus;
import lombok.*;

import java.sql.Time;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
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
    private List<ClassStudentDTO> classStudent;
    private String departmentName;

    public ClassDTO(Class aClass) {
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
                    .map(ClassStudentDTO::new)
                    .collect(Collectors.toList());
        }

        if (aClass.getCourse() != null && aClass.getCourse().getDepartment() != null) {
            this.departmentName = aClass.getCourse().getDepartment().getName();
        }
    }
}
