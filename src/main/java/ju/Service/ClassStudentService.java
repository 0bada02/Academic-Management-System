package ju.Service;

import ju.Exception.ResourceNotFoundException;
import ju.Model.Class.Class;
import ju.Model.ClassStudent.ClassStudent;
import ju.Model.ClassStudent.ClassStudentId;
import ju.Model.ClassStudent.Enum.Passed;
import ju.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Add a student to class
    public boolean addGrade(Integer departmentId, Integer courseId, Integer classId, Integer studentId, Double grade) {
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

        if (!studentRepository.existsById(studentId))
            throw new ResourceNotFoundException(String.format("Student not found with ID %d", studentId));

        ClassStudentId classStudentId = new ClassStudentId(classId, studentId);
        if (classStudentRepositry.existsByStudentId(studentId))
            throw new ResourceNotFoundException(String.format("Student with ID %d not exist in this classStudent with ID %s.", studentId, classStudentId));

        if (!classStudentRepositry.existsById(classStudentId)) {
            return false;
        } else {
            ClassStudent classStudent = classStudentRepositry.findById(classStudentId).get();
            try {
                classStudent.setGrade(grade);
                classStudent.setLetterGrades(Class.convertToLetter(grade));
                classStudent.setPassed(grade >= 45 ? Passed.PASS : Passed.FAILED);
                classStudentRepositry.save(classStudent);
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while saving the classStudent. Please check the provided data and try again.", e);
            }
            return true;
        }
    }
}