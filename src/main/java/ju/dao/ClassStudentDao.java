package ju.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import ju.model.ClassStudent.ClassStudent;
import ju.model.ClassStudent.Enum.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassStudentDao {
    private Integer studentId;
    private String studentName;
    private Double grade;
    private String letterGrades;
    private Passed passed;

    public ClassStudentDao(ClassStudent classStudent) {
        this.studentId = classStudent.getStudent().getId();
        this.studentName = classStudent.getStudent().getName();
        this.grade = classStudent.getGrade();
        this.letterGrades = classStudent.getLetterGrades();
        this.passed = classStudent.getPassed();
    }
}