package ju.Controller;

import ju.Model.Course.CourseDTO;
import ju.Model.Course.Course;
import ju.Repository.*;
import ju.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/department")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get a course by their ID
    @GetMapping("/{departmentId}/course/{id}")
    public ResponseEntity<CourseDTO> getCourseById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(departmentId, id));
    }

    // Get all courses for a given department
    @GetMapping("/{departmentId}/course")
    public ResponseEntity<List<CourseDTO>> getCoursesInDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(courseService.getCoursesInDepartment(departmentId));
    }

    // Get all courses for a collage
    @GetMapping("/course")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Add a new course to a department
    @PostMapping("/{departmentId}/course")
    public ResponseEntity<String> addCourse(
            @RequestBody Course course,
            @PathVariable Integer departmentId) {
        boolean isAdded = courseService.addCourse(course, departmentId);
        if (isAdded) {
            return new ResponseEntity<>("Course added successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("A Course already exists with this title!", HttpStatus.CONFLICT);
        }
    }

    // Delete a course by their ID
    @DeleteMapping("/{departmentId}/course/{id}")
    public ResponseEntity<String> deleteCourseById(
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isRemoved = courseService.deleteCourseById(departmentId, id);
        if (isRemoved) {
            return new ResponseEntity<>("Course deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Course with ID %d not found in department %d.", id, departmentId), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all courses in a given department
    @DeleteMapping("/{departmentId}/course")
    public ResponseEntity<String> deleteCoursesInDepartment(@PathVariable Integer departmentId) {
        boolean isRemoved = courseService.deleteCoursesInDepartment(departmentId);
        if (isRemoved) {
            return new ResponseEntity<>("Courses deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("No courses found in the %s department.",
                    departmentRepository.findById(departmentId).get().getName()), HttpStatus.NOT_FOUND);
        }
    }

    // Delete an all courses in a given collage
    @DeleteMapping("/course")
    public ResponseEntity<String> deleteAllCourses() {
        boolean isRemoved = courseService.deleteAllCourses();
        if (isRemoved) {
            return new ResponseEntity<>("Courses deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No courses found to delete.", HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing course by their ID
    @PutMapping("/{departmentId}/course/{id}")
    public ResponseEntity<String> updateCourse(
            @RequestBody Course course,
            @PathVariable Integer departmentId,
            @PathVariable Integer id) {
        boolean isUpdate = courseService.updateCourse(course, departmentId, id);
        if (isUpdate) {
            return new ResponseEntity<>("Course updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("A Course already exists with this title!", HttpStatus.CONFLICT);
        }
    }

    // Add Prerequisite a course
    @PostMapping("/{departmentId}/course/{id}/prerequisite/{prerequisiteId}")
    public ResponseEntity<String> addPrerequisite(
            @PathVariable Integer departmentId,
            @PathVariable Integer id,
            @PathVariable Integer prerequisiteId) {
        boolean isAdded = courseService.addPrerequisite(departmentId, id, prerequisiteId);
        if (isAdded) {
            return new ResponseEntity<>(String.format("Prerequisite with ID %d added on course with ID %d successfully!", prerequisiteId, id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Course with ID %d not found in department %d.", id, departmentId), HttpStatus.CONFLICT);
        }
    }

    // Delete Prerequisite from course
    @DeleteMapping("/{departmentId}/course/{id}/prerequisite/{prerequisiteId}")
    public ResponseEntity<String> deletePrerequisite(
            @PathVariable Integer departmentId,
            @PathVariable Integer id,
            @PathVariable Integer prerequisiteId) {
        boolean isRemove = courseService.deletePrerequisite(departmentId, id, prerequisiteId);
        if (isRemove) {
            return new ResponseEntity<>(String.format("Prerequisite deleted on course with ID %d successfully!", id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Course with ID %d not found in department %d.", id, departmentId), HttpStatus.CONFLICT);
        }
    }
}