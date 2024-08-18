package ju.Model.ClassStudent;

import ju.Model.ClassStudent.Enum.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassStudentDTO {
    private ClassStudentId id;
    private Double grade;
    private String letterGrades;
    private Passed passed;

    public ClassStudentDTO(ClassStudent classStudent) {
        this.id = classStudent.getId();
        this.grade = classStudent.getGrade();
        this.letterGrades = classStudent.getLetterGrades();
        this.passed = classStudent.getPassed();
    }
}