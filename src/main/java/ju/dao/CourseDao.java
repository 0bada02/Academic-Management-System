package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.Course;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDao {
    private Integer id;
    private String title;
    private Integer creditHours;
    private String departmentName;
    private List<ClassDao> classes;
    private List<String> instructorsName;
    private List<String> prerequisiteTitles;
    private List<String> coursesTitles;

    public CourseDao(Integer id, String title,
                     Integer creditHours, String departmentName) {
        this.id = id;
        this.title = title;
        this.creditHours = creditHours;
        this.departmentName = departmentName;
    }

    public CourseDao(Integer id, String title,
                     Integer creditHours) {
        this.id = id;
        this.title = title;
        this.creditHours = creditHours;
    }

    public CourseDao(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.creditHours = course.getCreditHours();
        this.departmentName = course.getDepartment().getName();

        // Transform related classes into DTOs
        this.classes = course.getClasses().stream()
                .map(c -> new ClassDao(c.getId(), c.getStartTime(), c.getEndTime(), c.getDaysOfWeek(), c.getRoomNumber(), c.getCapacity(), c.getSemester(), c.getYear(), c.getStatus()))
                .collect(Collectors.toList());

        // Extract instructors teaching the course (assuming the instructor is linked via class)
        this.instructorsName = (course.getClasses() != null) ? course.getClasses().stream()
                .map(aClass -> aClass.getInstructor() != null ? aClass.getInstructor().getName() : "Unknown")
                .distinct()
                .collect(Collectors.toList()) : new ArrayList<>();

        // Extract prerequisite course titles
        this.prerequisiteTitles = course.getPrerequisites().stream()
                .map(Course::getTitle)
                .collect(Collectors.toList());
    }
}