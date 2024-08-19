package ju.Model.Instructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ju.Model.Class.Class;
import ju.Model.Department.Department;
import lombok.*;

import java.util.*;

// Lombok's annotations for boilerplate code
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Instructor")
public class Instructor {

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Department_Id")
    private Department department;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.EAGER)
    private List<Class> classes = new ArrayList<>();
}