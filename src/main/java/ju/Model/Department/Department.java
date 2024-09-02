package ju.Model.Department;

import jakarta.persistence.*;
import ju.Model.Course.Course;
import ju.Model.Instructor.Instructor;
import ju.Model.Student.Student;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

// Lombok's annotations for boilerplate code
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Column(unique = true, nullable = false)
    private String name;

    @NotNull(message = "Total Hours Required cannot be null")
    @Column(nullable = false)
    private Integer totalHoursRequired;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Instructor> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();
}