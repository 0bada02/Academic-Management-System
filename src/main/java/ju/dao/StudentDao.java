package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.Student;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDao {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Integer totalHoursCompleted;
    private Integer totalHoursRemaining;
    private Double GPA;
    private String departmentName;

    public StudentDao(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.phone = student.getPhone();
        this.email = student.getEmail();
        this.address = student.getAddress();
        this.totalHoursCompleted = student.getTotalHoursCompleted();
        this.totalHoursRemaining = student.getTotalHoursRemaining();
        this.GPA = student.getGPA();
        this.departmentName = student.getDepartment().getName();
    }
}