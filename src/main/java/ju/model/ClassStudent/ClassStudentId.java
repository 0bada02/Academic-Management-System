package ju.model.ClassStudent;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ClassStudentId implements Serializable {
    private Integer classId;
    private Integer studentId;
}