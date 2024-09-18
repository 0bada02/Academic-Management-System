package ju.controller;

import ju.dao.ClassStudentDao;
import ju.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class ClassStudentController {

    @Autowired
    private ClassStudentService classStudentService;

    @GetMapping("/{departmentId}/course/{courseId}/class-student/{classId}")
    public ResponseEntity<List<ClassStudentDao>> getClassStudents(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId) {
        return ResponseEntity.ok(classStudentService.getStudentsInClass(departmentId, courseId, classId));
    }

    // Add a student to a class
    @PostMapping("/{departmentId}/course/{courseId}/addGrade/{classId}/{studentId}")
    public ResponseEntity<String> addGrade(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer studentId,
            @RequestParam Double grade) {
        boolean isAdded = classStudentService.addGrade(departmentId, courseId, classId, studentId, grade);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Grade %.2f has been successfully set for student with ID %d in class with ID %d."
                    , grade, studentId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The grade for this student has already been recorded.", HttpStatus.CONFLICT);
        }
    }
}