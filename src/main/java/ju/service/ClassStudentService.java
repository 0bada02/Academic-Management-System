package ju.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import ju.dao.ClassStudentDao;
import ju.exception.ResourceNotFoundException;
import ju.model.Class.Class;
import ju.model.ClassStudent.ClassStudent;
import ju.model.ClassStudent.ClassStudentId;
import ju.model.ClassStudent.Enum.Passed;
import ju.model.Student;
import ju.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassStudentService {

    @Autowired
    private ClassStudentRepositry classStudentRepositry;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<ClassStudentDao> getStudentsInClass(Integer departmentId, Integer courseId, Integer classId) {

        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        ArrayList<ClassStudent> classStudents = classStudentRepositry.findByaClassId(classId).orElseThrow(() -> new ResourceNotFoundException(String.format("Class not found with ID %d", classId)));

        return classStudents.stream()
                .map(ClassStudentDao::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    // Add a student to class
    public boolean addGrade(Integer departmentId, Integer courseId, Integer classId, Integer studentId, Double grade) {
        ClassStudentId classStudentId = new ClassStudentId(classId, studentId);

        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!classRepository.existsById(classId))
            throw new ResourceNotFoundException(String.format("Class not found with ID %d", classId));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        if (!classRepository.existsByIdAndCourseId(classId, courseId))
            throw new ResourceNotFoundException(String.format("class with ID %d not found in %s course.", classId, courseRepository.findById(courseId).get().getTitle()));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(String.format("Student not found with ID %d", studentId)));

        if (!classStudentRepositry.existsByStudentId(studentId))
            throw new ResourceNotFoundException(String.format("Student with ID %d not exist in this classStudent with ID %s.", studentId, classStudentId));

        if (!classStudentRepositry.existsById(classStudentId))
            throw new ResourceNotFoundException(String.format("classStudent not found with ID %d", studentId));

        ClassStudent classStudent = classStudentRepositry.findById(classStudentId).get();
        if (classStudent.getGrade() != null) {
            return false;
        } else {
            try {
                Integer courseCreditHours = classStudent.getAClass().getCourse().getCreditHours();
                classStudent.setGrade(grade);
                classStudent.setPassed(grade >= 45 ? Passed.PASS : Passed.FAILED);
                classStudent.setLetterGrades(Class.convertToLetter(grade));
                student.updateGPA(classStudent);
                if (grade >= 45) {
                    student.setTotalHoursCompleted(student.getTotalHoursCompleted() + courseCreditHours);
                    student.setTotalHoursRemaining(student.getDepartment().getTotalHoursRequired() - student.getTotalHoursCompleted());
                } else {
                    student.setTotalHoursFailed(student.getTotalHoursFailed() + courseCreditHours);
                }
                classStudentRepositry.save(classStudent);
                studentRepository.save(student);
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while saving the classStudent. Please check the provided data and try again.", e);
            }
            return true;
        }
    }
}