package ju.Model.ClassStudent;

import jakarta.persistence.*;
import ju.Model.Class.Class;
import ju.Model.ClassStudent.Enum.*;
import ju.Model.Student.Student;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Class_Student")
public class ClassStudent {

    @EmbeddedId
    private ClassStudentId id = new ClassStudentId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("classId")
    @JoinColumn(name = "Class_Id")
    private Class aClass;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("studentId")
    @JoinColumn(name = "Student_Id")
    private Student student;

    @Column(name = "Grades")
    private Double grade;

    @Column(name = "Letter_Grades")
    private String letterGrades;

    @Enumerated(EnumType.STRING)
    @Column(name = "Passed")
    private Passed passed;
}