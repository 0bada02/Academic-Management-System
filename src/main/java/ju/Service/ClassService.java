package ju.Service;

import jakarta.transaction.Transactional;
import ju.Model.Class.ClassDTO;
import ju.Exception.ResourceNotFoundException;
import ju.Model.Class.Class;
import ju.Model.Class.Enum.ClassStatus;
import ju.Model.Course.Course;
import ju.Model.Instructor.Instructor;
import ju.Model.Student.Student;
import ju.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service // Service annotation
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    // Get class by their ID
    public ClassDTO getClassById(Integer departmentId, Integer courseId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!classRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Class not found with ID %d", id));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        Class aClass = classRepository.findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("class with ID %d not found in %s course.", id, courseRepository.findById(courseId).get().getTitle())));

        return new ClassDTO(aClass);
    }

    // Get all Classes for a given department
    public List<ClassDTO> getClassesInCourse(Integer departmentId, Integer courseId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        List<Class> classes = classRepository.findByCourseId(courseId);
        if (classes.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No classes found in %s course.", courseRepository.findById(courseId).get().getTitle()));
        }
        return classes.stream()
                .map(ClassDTO::new)
                .collect(Collectors.toList());
    }

    // Get all Classes for a given department
    public List<ClassDTO> getClassesInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty())
            throw new ResourceNotFoundException(String.format("No courses found in department %d.", departmentId));

        List<Class> classes = new ArrayList<>();
        for (Course course : courses)
            classes.addAll(course.getClasses());

        return classes.stream()
                .map(ClassDTO::new)
                .collect(Collectors.toList());
    }

    // Get all classes for a collage
    public List<ClassDTO> getAllClasses() {
        if (departmentRepository.count() == 0)
            throw new ResourceNotFoundException("No departments found.");

        List<Class> classes = classRepository.findAll();
        if (classes.isEmpty())
            throw new ResourceNotFoundException("No classes found in the college.");

        return classes.stream()
                .map(ClassDTO::new)
                .collect(Collectors.toList());
    }

    // Add new a course
    public boolean addClass(Class aClass, Integer departmentId, Integer courseId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        if (classRepository.existsByYearAndSemesterAndRoomNumberAndDaysOfWeekAndStartTimeAndEndTimeOrOverlapping(
                aClass.getYear(),
                aClass.getSemester(),
                aClass.getRoomNumber(),
                aClass.getDaysOfWeek(),
                aClass.getStartTime(),
                aClass.getEndTime()
        )) {
            return false;
        } else {
            aClass.setCourse(courseRepository.findById(courseId).get());
            classRepository.save(aClass);
            return true;
        }
    }

    // Delete a class by their ID
    public boolean deleteClassById(Integer departmentId, Integer courseId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        if (!classRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Class not found with ID %d", id));

        if (classRepository.existsByIdAndCourseId(id, courseId)) {
            return false;
        } else {
            classRepository.deleteById(id);
            return true;
        }
    }

    // Delete all classes in a given department
    public boolean deleteClassesInCourse(Integer departmentId, Integer courseId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        List<Class> classes = classRepository.findByCourseId(courseId);
        if (classes.isEmpty()) {
            return false;
        } else {
            classRepository.deleteAll(classes);
            return true;
        }
    }

    // Delete all classes in a given department
    public boolean deleteClassesInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty()) {
            return false;
        } else {
            for (Course course : courses) {
                List<Class> classes = classRepository.findByCourseId(course.getId());
                classRepository.deleteAll(classes);
            }
            return true;
        }
    }

    // Delete all classes in a given collage
    public boolean deleteAllClasses() {
        if (classRepository.count() == 0) {
            return false;
        } else {
            classRepository.deleteAll();
            return true;
        }
    }

    // Update class details
    public boolean updateClass(Class aClass, Integer departmentId, Integer courseId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", courseId));

        Class updatingClass = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Class not found with ID %d.", id)));

        if (!courseRepository.existsByIdAndDepartmentId(courseId, departmentId))
            throw new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", courseId, departmentId));

        if (classRepository.existsByIdAndCourseId(id, courseId))
            throw new ResourceNotFoundException(String.format("Class with ID %d not found in Course %d.", id, courseId));
        try {
            updatingClass.setStartTime(aClass.getStartTime());
            updatingClass.setEndTime(aClass.getEndTime());
            updatingClass.setDaysOfWeek(aClass.getDaysOfWeek());
            updatingClass.setRoomNumber(aClass.getRoomNumber());
            updatingClass.setCapacity(aClass.getCapacity());
            updatingClass.setSemester(aClass.getSemester());
            updatingClass.setYear(aClass.getYear());
            classRepository.save(updatingClass);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Transactional
    // Add a student to class
    public boolean addStudentToClass(Integer departmentId, Integer courseId, Integer classId, Integer studentId, Double grade) {
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

        if (!studentRepository.existsById(studentId)) {
            return false;
        } else {
            Student student = studentRepository.findById(studentId).get();
            Class aClass = classRepository.findById(classId).get();
            aClass.addStudent(student, grade);
            aClass.setStatus(aClass.getClassStudents().size() >= aClass.getCapacity() ? ClassStatus.CLOSE : ClassStatus.OPEN);
            try {
                classRepository.save(aClass);
                studentRepository.save(student);
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while saving the class or student. Please check the provided data and try again.", e);
            }
            return true;
        }
    }

    @Transactional
    // Remove a student to class
    public boolean removeStudentFromClass(Integer departmentId, Integer courseId, Integer classId, Integer studentId) {
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

        if (!studentRepository.existsById(studentId)) {
            return false;
        } else {
            Student student = studentRepository.findById(studentId).get();
            Class aClass = classRepository.findById(classId).get();
            aClass.removeStudent(student);
            aClass.setStatus(aClass.getClassStudents().size() >= aClass.getCapacity() ? ClassStatus.CLOSE : ClassStatus.OPEN);
            try {
                classRepository.save(aClass);
                studentRepository.save(student);
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while saving the class or student. Please check the provided data and try again.", e);
            }
            return true;
        }
    }

    // Add instructor to class
    public boolean addInstructorToClass(Integer departmentId, Integer courseId, Integer classId, Integer instructorId) {
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

        if (!instructorRepository.existsById(instructorId)) {
            return false;
        } else {
            Instructor instructor = instructorRepository.findById(instructorId).get();
            Class aClass = classRepository.findById(classId).get();
            aClass.addInstructor(instructor);
            classRepository.save(aClass);
            return true;
        }
    }

    // Remove instructor to class
    public boolean changeInstructorInClass(Integer departmentId, Integer courseId, Integer classId, Integer instructorId) {
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

        if (!instructorRepository.existsById(instructorId)) {
            return false;
        } else {
            Instructor instructor = instructorRepository.findById(instructorId).get();
            Class aClass = classRepository.findById(classId).get();
            aClass.changeInstructor(instructor);
            classRepository.save(aClass);
            return true;
        }
    }
}