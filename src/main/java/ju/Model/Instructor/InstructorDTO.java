package ju.Model.Instructor;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructorDTO {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String departmentName;

    public InstructorDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.name = instructor.getName();
        this.phone = instructor.getPhone();
        this.email = instructor.getEmail();
        this.address = instructor.getAddress();
        this.departmentName = instructor.getDepartment().getName();
    }
}