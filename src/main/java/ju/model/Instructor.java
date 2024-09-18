package ju.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ju.model.Class.Class;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Instructors")
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_Id", referencedColumnName = "Id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Department_Id")
    private Department department;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.EAGER)
    private List<Class> classes;

    public void generateEmail() {
        String[] names = name.split(" ");
        email = names[0].concat("@ju.edu.jo");
    }
}