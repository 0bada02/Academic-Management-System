package ju.Service;

import jakarta.transaction.Transactional;
import ju.Model.Department.DepartmentDTO;
import ju.Exception.ResourceNotFoundException;
import ju.Model.Department.Department;
import ju.Model.Student.Student;
import ju.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Service annotation
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Get department by ID
    public DepartmentDTO getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department not found with ID %d.", id)));

        return new DepartmentDTO(department);
    }

    // Get all departments
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("No departments found.");
        } else {
            List<DepartmentDTO> departmentDTOS = new ArrayList<>();
            for (Department department : departments)
                departmentDTOS.add(new DepartmentDTO(department));

            return departmentDTOS;
        }
    }

    // Add new department
    public boolean addDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            return false;
        } else {
            departmentRepository.save(department);
            return true;
        }
    }

    // Delete department by ID
    public boolean deleteDepartmentById(Integer id) {
        if (!departmentRepository.existsById(id)) {
            return false;
        } else {
            departmentRepository.deleteById(id);
            return true;
        }
    }

    // Delete all departments
    public boolean deleteAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            return false;
        } else {
            departmentRepository.deleteAll();
            return true;
        }
    }


    @Transactional
    // Update department details
    public boolean updateDepartment(Department department, Integer id) {
        Department updatingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department not found with ID %d.", id)));

        try {
            updatingDepartment.setName(department.getName());
            updatingDepartment.setTotalHoursRequired(department.getTotalHoursRequired());
            departmentRepository.save(updatingDepartment);

            // Update totalHoursRemaining for all students in the department
            List<Student> students = studentRepository.findByDepartmentId(id);
            for (Student student : students)
                student.setTotalHoursRemaining(department.getTotalHoursRequired() - student.getTotalHoursCompleted());

            studentRepository.saveAll(students);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("A department already exists with the same name!");
        } catch (Exception e) {
            return false;
        }
    }
}