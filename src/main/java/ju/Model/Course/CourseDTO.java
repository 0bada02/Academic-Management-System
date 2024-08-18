package ju.Model.Course;

import ju.Model.Class.ClassDTO;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Integer id;
    private String title;
    private Integer creditHours;
    private String departmentName; // Related department's name
    private List<ClassDTO> classes = new ArrayList<>(); // List of class DTOs related to this course
    private List<String> instructorsName = new ArrayList<>(); // List of instructor DTOs teaching this course
    private List<String> prerequisiteTitles = new ArrayList<>(); // Titles of prerequisite courses

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.creditHours = course.getCreditHours();
        this.departmentName = course.getDepartment().getName(); // Extract department name

        // Transform related classes into DTOs
        this.classes = course.getClasses().stream()
                .map(ClassDTO::new)
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