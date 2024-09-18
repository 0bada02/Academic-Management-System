package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.Department;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDao {
    private Integer id;
    private String name;
    private Integer totalHoursRequired;
    private List<StudentDao> students;
    private List<CourseDao> courses;
    private List<InstructorDao> instructors;

    public DepartmentDao(Integer id, String name, Integer totalHoursRequired) {
        this.id = id;
        this.name = name;
        this.totalHoursRequired = totalHoursRequired;
    }

    public DepartmentDao(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.totalHoursRequired = department.getTotalHoursRequired();

        this.students = department.getStudents().stream()
                .map(StudentDao::new)
                .collect(Collectors.toList());

        this.courses = department.getCourses().stream()
                .map(CourseDao::new)
                .collect(Collectors.toList());

        this.instructors = department.getInstructors().stream()
                .map(InstructorDao::new)
                .collect(Collectors.toList());
    }
}