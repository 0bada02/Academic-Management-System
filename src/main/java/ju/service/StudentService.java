package ju.service;

import ju.dao.ClassDao;
import ju.dao.CourseDao;
import ju.dao.StudentDao;
import ju.exception.ResourceNotFoundException;
import ju.model.Department;
import ju.model.Student;
import ju.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get a student by ID
    public StudentDao getStudentById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d", id));

        Student student = studentRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Student with ID %d not found in department %d.", id, departmentId)));

        return new StudentDao(student);
    }

    // Get student-course by ID
    public ArrayList<CourseDao> getCoursesForStudent(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d", id));

        Student student = studentRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Student with ID %d not found in department %d.", id, departmentId)));

        return student.getClassStudents().stream()
                .map(cs -> new CourseDao(cs.getAClass().getCourse().getId(), cs.getAClass().getCourse().getTitle(),
                        cs.getAClass().getCourse().getCreditHours()))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Get student-class by ID
    public ArrayList<ClassDao> getClassesForStudent(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d", id));

        Student student = studentRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Student with ID %d not found in department %d.", id, departmentId)));

        return student.getClassStudents().stream()
                .map(cs -> new ClassDao(cs.getId().getClassId(), cs.getAClass().getStartTime(), cs.getAClass().getEndTime(), cs.getAClass().getDaysOfWeek(),
                        cs.getAClass().getRoomNumber(), cs.getAClass().getSemester(), cs.getAClass().getYear()))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Get all students for a given department
    public List<StudentDao> getStudentsInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Student> students = studentRepository.findByDepartmentId(departmentId);
        if (students.isEmpty())
            throw new ResourceNotFoundException(String.format("No students found in department with ID %d.", departmentId));

        return students.stream()
                .map(StudentDao::new)
                .collect(Collectors.toList());
    }

    // Get all students for a collage
    public List<StudentDao> getAllStudents() {
        if (departmentRepository.count() == 0)
            throw new ResourceNotFoundException("No departments found.");

        List<Student> students = studentRepository.findAll();
        if (students.isEmpty())
            throw new ResourceNotFoundException("No students found in the college.");

        return students.stream()
                .map(StudentDao::new)
                .collect(Collectors.toList());
    }

    // Add new a student
    public boolean addStudent(Student student, Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (studentRepository.existsByNameOrPhoneOrEmail(
                student.getName(),
                student.getPhone(),
                student.getEmail())) {
            return false;
        } else {
            student.setDepartment(departmentRepository.findById(departmentId).get());
            student.setTotalHoursRemaining(student.getDepartment().getTotalHoursRequired() - student.getTotalHoursCompleted());
            student.generateEmail();
            studentRepository.save(student);
            return true;
        }
    }

    // Delete a student by their ID
    public boolean deleteStudentById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d.", id));

        if (!studentRepository.existsByIdAndDepartmentId(id, departmentId)) {
            return false;
        } else {
            studentRepository.deleteById(id);
            return true;
        }
    }

    // Delete all students in a given department
    public boolean deleteStudentsInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Student> students = studentRepository.findByDepartmentId(departmentId);
        if (students.isEmpty()) {
            return false;
        } else {
            studentRepository.deleteAll(students);
            return true;
        }
    }

    // Delete all students in a given collage
    public boolean deleteAllStudents() {
        if (studentRepository.count() == 0) {
            return false;
        } else {
            studentRepository.deleteAll();
            return true;
        }
    }

    // Update student details
    public boolean updateStudent(Student student, Integer departmentId, Integer id) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId)));

        Student updatingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Student not found with ID %d.", id)));

        if (!studentRepository.existsByIdAndDepartmentId(id, departmentId))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d in %s department.", id, department.getName()));

        try {
            updatingStudent.setName(student.getName());
            updatingStudent.setPhone(student.getPhone());
            updatingStudent.setEmail(student.getEmail());
            updatingStudent.setAddress(student.getAddress());
            updatingStudent.setTotalHoursCompleted(student.getTotalHoursCompleted());
            updatingStudent.setTotalHoursRemaining(student.getTotalHoursRemaining());
            updatingStudent.setGPA(student.getGPA());
            updatingStudent.setTotalHoursRemaining(updatingStudent.getDepartment().getTotalHoursRequired() - student.getTotalHoursCompleted());
            studentRepository.save(updatingStudent);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}