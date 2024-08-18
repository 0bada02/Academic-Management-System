package ju.Controller;

import ju.Model.Department.DepartmentDTO;
import ju.Model.Department.Department;
import ju.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Get a department by its ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // Get all departments
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping
    public ResponseEntity<String> addDepartment(@RequestBody Department department) {
        boolean isAdded = departmentService.addDepartment(department);
        if (isAdded) {
            return new ResponseEntity<>("Department added successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("A department already exists!", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartmentById(@PathVariable Integer id) {
        boolean isRemoved = departmentService.deleteDepartmentById(id);
        if (isRemoved) {
            return new ResponseEntity<>("Department deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format("Department not found with ID %d.", id), HttpStatus.NOT_FOUND);
        }
    }

    // Delete all departments
    @DeleteMapping
    public ResponseEntity<String> deleteAllDepartments() {
        boolean isRemoved = departmentService.deleteAllDepartments();
        if (isRemoved) {
            return new ResponseEntity<>("Departments deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No departments found to delete.", HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing department by its ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(@RequestBody Department department, @PathVariable Integer id) {
        boolean isUpdate = departmentService.updateDepartment(department, id);
        if (isUpdate) {
            return new ResponseEntity<>("Department updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating department and students", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}