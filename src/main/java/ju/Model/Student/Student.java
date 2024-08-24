package ju.Model.Student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ju.Model.Class.Class;
import ju.Model.ClassStudent.ClassStudent;
import ju.Model.Department.Department;
import lombok.*;

import java.util.*;

// Lombok's annotations for boilerplate code
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank(message = "Phone is mandatory")
    @Column(unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "Email is mandatory")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "Address cannot be null")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Total Hours Completed cannot be null")
    @Column(nullable = false)
    private Integer totalHoursCompleted = 0;

    @NotNull(message = "Total Hours Failed cannot be null")
    @Column(nullable = false)
    private Integer totalHoursFailed = 0;

    @NotNull(message = "Total Hours Remaining cannot be null")
    @Column(nullable = false)
    private Integer totalHoursRemaining;

    @NotNull(message = "GPA cannot be null")
    @Column(nullable = false)
    private Double GPA = 0.0;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Department_Id")
    private Department department;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ClassStudent> classStudents = new ArrayList<>();

    public void updateGPA(ClassStudent cs) {
        Integer courseCreditHours = cs.getAClass().getCourse().getCreditHours();
        Integer totalHours = totalHoursCompleted + totalHoursFailed;
        double qualityPoints = GPA * totalHours + courseCreditHours * Class.convertToGPA(cs.getLetterGrades());
        GPA = qualityPoints / (totalHours + courseCreditHours);
    }
}