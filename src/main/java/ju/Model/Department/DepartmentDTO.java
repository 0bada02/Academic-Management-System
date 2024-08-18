package ju.Model.Department;

import ju.Model.Instructor.InstructorDTO;
import ju.Model.Course.CourseDTO;
import ju.Model.Student.StudentDTO;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private Integer id;
    private String name;
    private List<StudentDTO> students = new ArrayList<>();
    private List<CourseDTO> courses = new ArrayList<>();
    private List<InstructorDTO> instructors = new ArrayList<>();

    public DepartmentDTO(Department department) {
        this.id = department.getId();
        this.name = department.getName();

        // Transform entities into DTOs
        this.students = department.getStudents().stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());

        this.courses = department.getCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());

        this.instructors = department.getInstructors().stream()
                .map(InstructorDTO::new)
                .collect(Collectors.toList());
    }
}