package ju.Controller;

import ju.Service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class ClassStudentController {

    @Autowired
    private ClassStudentService classStudentService;

    // Add a student to a class
    @PostMapping("/{departmentId}/course/{courseId}/addGrade/{classId}/{studentId}")
    public ResponseEntity<String> addGrade(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer studentId,
            @RequestParam(required = false) Double grade) {
        boolean isAdded = classStudentService.addGrade(departmentId, courseId, classId, studentId, grade);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Grade %.2f has been successfully set for student with ID %d in class with ID %d."
                    , grade, studentId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("classStudent not found with ID %d", studentId), HttpStatus.NOT_FOUND);
        }
    }
}