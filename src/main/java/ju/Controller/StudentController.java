package ju.Controller;

import ju.Model.Student.StudentDTO;
import ju.Model.Student.Student;
import ju.Repository.*;
import ju.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/department")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get a student by their ID
    @GetMapping("/{departmentId}/student/{id}")
    public ResponseEntity<StudentDTO> getStudentById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(departmentId, id));
    }

    // Get all students for a given department
    @GetMapping("/{departmentId}/student")
    public ResponseEntity<List<StudentDTO>> getStudentsInDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(studentService.getStudentsInDepartment(departmentId));
    }

    // Get all students for a collage
    @GetMapping("/student")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Add a new student to a department
    @PostMapping("/{departmentId}/student")
    public ResponseEntity<String> addStudent(
            @RequestBody Student student,
            @PathVariable Integer departmentId) {
        boolean isAdded = studentService.addStudent(student, departmentId);
        if (isAdded) {
            return new ResponseEntity<>("Student added successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("A student already exists!", HttpStatus.CONFLICT);
        }
    }

    // Delete a student by their ID
    @DeleteMapping("/{departmentId}/student/{id}")
    public ResponseEntity<String> deleteStudentById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isRemoved = studentService.deleteStudentById(departmentId, id);
        if (isRemoved) {
            return new ResponseEntity<>("Student deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Student with ID %d not found in department %d.", id, departmentId), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all students in a given department
    @DeleteMapping("/{departmentId}/student")
    public ResponseEntity<String> deleteStudentsInDepartment(@PathVariable Integer departmentId) {
        boolean isRemoved = studentService.deleteStudentsInDepartment(departmentId);
        if (isRemoved) {
            return new ResponseEntity<>("Students deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("No students found in the %s department.",
                    departmentRepository.findById(departmentId).get().getName()), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all students in a given collage
    @DeleteMapping("/student")
    public ResponseEntity<String> deleteAllStudents() {
        boolean isRemoved = studentService.deleteAllStudents();
        if (isRemoved) {
            return new ResponseEntity<>("Students deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No students found to delete.", HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing student by their ID
    @PutMapping("/{departmentId}/student/{id}")
    public ResponseEntity<String> updateStudent(
            @RequestBody Student student,
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isUpdate = studentService.updateStudent(student, departmentId, id);
        if (isUpdate) {
            return new ResponseEntity<>("Student updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("A student already exists!", HttpStatus.CONFLICT);
        }
    }
}