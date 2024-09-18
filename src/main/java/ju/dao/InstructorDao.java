package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.Instructor;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructorDao {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String departmentName;


    public InstructorDao(Instructor instructor) {
        this.id = instructor.getId();
        this.name = instructor.getName();
        this.phone = instructor.getPhone();
        this.email = instructor.getEmail();
        this.address = instructor.getAddress();
        this.departmentName = instructor.getDepartment().getName();
    }
}