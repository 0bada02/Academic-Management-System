package ju.Service;

import ju.Model.Instructor.InstructorDTO;
import ju.Exception.ResourceNotFoundException;
import ju.Model.Department.Department;
import ju.Model.Instructor.Instructor;
import ju.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service // Service annotation
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get instructor by ID
    public InstructorDTO getInstructorById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!instructorRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Instructor not found with ID %d", id));

        Instructor instructor = instructorRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Instructor with ID %d not found in department %d.", id, departmentId)));

        return new InstructorDTO(instructor);
    }

    // Get all instructors by department ID
    public List<InstructorDTO> getInstructorsInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Instructor> instructors = instructorRepository.findByDepartmentId(departmentId);
        if (instructors.isEmpty())
            throw new ResourceNotFoundException(String.format("No instructors found in department with ID %d.", departmentId));

        return instructors.stream()
                .map(InstructorDTO::new)
                .collect(Collectors.toList());
    }

    // Get all Instructors for a collage
    public List<InstructorDTO> getAllInstructors() {
        if (departmentRepository.count() == 0)
            throw new ResourceNotFoundException("No departments found.");

        List<Instructor> instructors = instructorRepository.findAll();
        if (instructors.isEmpty())
            throw new ResourceNotFoundException("No instructors found in the college.");

        return instructors.stream()
                .map(InstructorDTO::new)
                .collect(Collectors.toList());
    }

    // Add new an instructor
    public boolean addInstructor(Instructor instructor, Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        // Check if another instructor with the same name, phone, or email already exists
        boolean instructorExists = instructorRepository.existsByNameOrPhoneOrEmail(instructor.getName(), instructor.getPhone(), instructor.getEmail());

        if (instructorExists) {
            return false;
        } else {
            instructor.setDepartment(departmentRepository.findById(departmentId).get());
            instructorRepository.save(instructor);
            return true;
        }
    }

    // Delete an instructor by their ID
    public boolean deleteInstructorById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!instructorRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Instructor not found with ID %d.", id));

        if (instructorRepository.existsByIdAndDepartmentId(id, departmentId)) {
            return false;
        } else {
            instructorRepository.deleteById(id);
            return true;
        }
    }

    // Delete all instructors in a given department
    public boolean deleteInstructorsInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Instructor> instructors = instructorRepository.findByDepartmentId(departmentId);
        if (instructors.isEmpty()) {
            return false;
        } else {
            instructorRepository.deleteAll(instructors);
            return true;
        }
    }

    // Delete all instructors in a given collage
    public boolean deleteAllInstructors() {
        if (instructorRepository.count() == 0) {
            return false;
        } else {
            instructorRepository.deleteAll();
            return true;
        }
    }

    // Update instructor details
    public boolean updateInstructor(Instructor instructor, Integer departmentId, Integer id) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId)));

        Instructor updatingInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Instructor not found with ID %d.", id)));

        if (instructorRepository.existsByIdAndDepartmentId(id, departmentId))
            throw new ResourceNotFoundException(String.format("Instructor not found with ID %d in %s department.", id, department.getName()));

        try {
            updatingInstructor.setName(instructor.getName());
            updatingInstructor.setPhone(instructor.getPhone());
            updatingInstructor.setEmail(instructor.getEmail());
            updatingInstructor.setAddress(instructor.getAddress());
            instructorRepository.save(updatingInstructor);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}