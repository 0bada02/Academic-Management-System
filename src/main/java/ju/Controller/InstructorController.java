package ju.Controller;

import ju.Model.Instructor.InstructorDTO;
import ju.Model.Instructor.Instructor;
import ju.Repository.*;
import ju.Service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/department")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get an instructor by their ID
    @GetMapping("/{departmentId}/instructor/{id}")
    public ResponseEntity<InstructorDTO> getInstructorById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        return ResponseEntity.ok(instructorService.getInstructorById(departmentId, id));
    }

    // Get all instructors for a given department
    @GetMapping("/{departmentId}/instructor")
    public ResponseEntity<List<InstructorDTO>> getInstructorsInDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(instructorService.getInstructorsInDepartment(departmentId));
    }

    // Get all instructors for a collage
    @GetMapping("/instructor")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    // Add a new instructor to a department
    @PostMapping("/{departmentId}/instructor")
    public ResponseEntity<String> addInstructor(
            @RequestBody Instructor instructor,
            @PathVariable Integer departmentId) {
        boolean isAdded = instructorService.addInstructor(instructor, departmentId);
        if (isAdded) {
            return new ResponseEntity<>("Instructor added successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("A instructor already exists!", HttpStatus.CONFLICT);
        }
    }

    // Delete an instructor by their ID
    @DeleteMapping("/{departmentId}/instructor/{id}")
    public ResponseEntity<String> deleteInstructorById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isRemoved = instructorService.deleteInstructorById(departmentId, id);
        if (isRemoved) {
            return new ResponseEntity<>("Instructor deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Instructor with ID %d not found in department %d.", id, departmentId), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all instructors in a given department
    @DeleteMapping("/{departmentId}/instructor")
    public ResponseEntity<String> deleteInstructorsInDepartment(@PathVariable Integer departmentId) {
        boolean isRemoved = instructorService.deleteInstructorsInDepartment(departmentId);
        if (isRemoved) {
            return new ResponseEntity<>("Instructors deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("No instructors found in the %s department.",
                    departmentRepository.findById(departmentId).get().getName()), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all instructors in a given collage
    @DeleteMapping("/instructor")
    public ResponseEntity<String> deleteAllInstructors() {
        boolean isRemoved = instructorService.deleteAllInstructors();
        if (isRemoved) {
            return new ResponseEntity<>("Instructors deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No instructors found to delete.", HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing instructor by their ID
    @PutMapping("/{departmentId}/instructor/{id}")
    public ResponseEntity<String> updateInstructor(
            @RequestBody Instructor instructor,
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isUpdate = instructorService.updateInstructor(instructor, departmentId, id);
        if (isUpdate) {
            return new ResponseEntity<>("Instructor updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("A Instructor already exists!", HttpStatus.CONFLICT);
        }
    }
}