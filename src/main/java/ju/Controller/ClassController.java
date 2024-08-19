package ju.Controller;

import ju.Model.Class.ClassDTO;
import ju.Model.Class.Class;
import ju.Repository.*;
import ju.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get a class by their ID
    @GetMapping("/{departmentId}/course/{courseId}/class/{id}")
    public ResponseEntity<ClassDTO> getClassById(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer id) {
        return ResponseEntity.ok(classService.getClassById(departmentId, courseId, id));
    }

    // Get all classes for a given course
    @GetMapping("/{departmentId}/course/{courseId}/class")
    public ResponseEntity<List<ClassDTO>> getClassesInCourse(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId) {
        return ResponseEntity.ok(classService.getClassesInCourse(departmentId, courseId));
    }

    // Get all classes for a given department
    @GetMapping("/{departmentId}/class")
    public ResponseEntity<List<ClassDTO>> getClassesInDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(classService.getClassesInDepartment(departmentId));
    }

    // Get all courses for a collage
    @GetMapping("/class")
    public ResponseEntity<List<ClassDTO>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    // Add a new class to a course
    @PostMapping("/{departmentId}/course/{courseId}/class")
    public ResponseEntity<String> addCourse(
            @RequestBody Class aClass,
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId) {
        boolean isAdded = classService.addClass(aClass, departmentId, courseId);
        if (isAdded) {
            return new ResponseEntity<>("Class added successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("A Class already exists!", HttpStatus.CONFLICT);
        }
    }

    // Delete a class by their ID
    @DeleteMapping("/{departmentId}/course/{courseId}/class/{id}")
    public ResponseEntity<String> deleteClassById(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer id) {
        boolean isRemoved = classService.deleteClassById(departmentId, courseId, id);
        if (isRemoved) {
            return new ResponseEntity<>("Class deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Class with ID %d not found in Course %d.", id, courseId), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all classes in a given course
    @DeleteMapping("/{departmentId}/course/{courseId}/class")
    public ResponseEntity<String> deleteClassesInCourse(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId) {
        boolean isRemoved = classService.deleteClassesInCourse(departmentId, courseId);
        if (isRemoved) {
            return new ResponseEntity<>("Classes deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("No classes found in the %s course.",
                    departmentRepository.findById(departmentId).get().getName()), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all classes in a given department
    @DeleteMapping("/{departmentId}/class")
    public ResponseEntity<String> deleteClassesInDepartment(@PathVariable Integer departmentId) {
        boolean isRemoved = classService.deleteClassesInDepartment(departmentId);
        if (isRemoved) {
            return new ResponseEntity<>("Classes deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("No classes found in the %s department.",
                    departmentRepository.findById(departmentId).get().getName()), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all classes in a given collage
    @DeleteMapping("/class")
    public ResponseEntity<String> deleteAllClasses() {
        boolean isRemoved = classService.deleteAllClasses();
        if (isRemoved) {
            return new ResponseEntity<>("Classes deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No classes found to delete.", HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing class by their ID
    @PutMapping("/{departmentId}/course/{courseId}/class/{id}")
    public ResponseEntity<String> updateClass(
            @RequestBody Class aClass,
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer id) {
        boolean isUpdate = classService.updateClass(aClass, departmentId, courseId, id);
        if (isUpdate) {
            return new ResponseEntity<>("Class updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("A class already exists!", HttpStatus.CONFLICT);
        }
    }

    // Add a student to a class
    @PostMapping("/{departmentId}/course/{courseId}/addStudent/{classId}/{studentId}")
    public ResponseEntity<String> addStudentToClass(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer studentId,
            @RequestParam(required = false) Double grade) {
        boolean isAdded = classService.addStudentToClass(departmentId, courseId, classId, studentId, grade);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Student with ID %d added on class with ID %d successfully!", studentId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Student not found with ID %d", studentId), HttpStatus.CONFLICT);
        }
    }

    // Remove a student to a class
    @DeleteMapping("/{departmentId}/course/{courseId}/removeStudent/{classId}/{studentId}")
    public ResponseEntity<String> removeStudentFromClass(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer studentId) {
        boolean isAdded = classService.removeStudentFromClass(departmentId, courseId, classId, studentId);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Student with ID %d deleted from class with ID %d successfully!", studentId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Student not found with ID %d", studentId), HttpStatus.CONFLICT);
        }
    }

    // Add an instructor to a class
    @PostMapping("/{departmentId}/course/{courseId}/addInstructor/{classId}/{instructorId}")
    public ResponseEntity<String> addInstructorToClass(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer instructorId) {
        boolean isAdded = classService.addInstructorToClass(departmentId, courseId, classId, instructorId);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Instructor with ID %d added on class with ID %d successfully!", instructorId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Instructor not found with ID %d", instructorId), HttpStatus.CONFLICT);
        }
    }

    // Remove an instructor to a class
    @PutMapping("/{departmentId}/course/{courseId}/changeInstructor/{classId}/{instructorId}")
    public ResponseEntity<String> changeInstructorInClass(
            @PathVariable Integer departmentId,
            @PathVariable Integer courseId,
            @PathVariable Integer classId,
            @PathVariable Integer instructorId) {
        boolean isAdded = classService.changeInstructorInClass(departmentId, courseId, classId, instructorId);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Instructor with ID %d changed on class with ID %d successfully!", instructorId, classId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Instructor not found with ID %d", instructorId), HttpStatus.CONFLICT);
        }
    }
}