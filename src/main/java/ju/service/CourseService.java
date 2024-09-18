package ju.service;

import ju.dao.CourseDao;
import ju.exception.ResourceNotFoundException;
import ju.model.Course;
import ju.model.Department;
import ju.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service // Service annotation
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get course by their ID
    public CourseDao getCourseById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", id));

        Course course = courseRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", id, departmentId)));

        return new CourseDao(course.getId(), course.getTitle(), course.getCreditHours(), course.getDepartment().getName());
    }

    // Get course-class by their ID
    public CourseDao getCourseById_Class(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department with ID %d not found", departmentId));

        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d", id));

        Course course = courseRepository.findByIdAndDepartmentId(id, departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Course with ID %d not found in department %d.", id, departmentId)));

        return new CourseDao(course);
    }

    // Get all courses for a given department
    public List<CourseDao> getCoursesInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No courses found in department with ID %d.", departmentId));
        }
        return courses.stream()
                .map(c -> new CourseDao(c.getId(), c.getTitle(), c.getCreditHours(), c.getDepartment().getName()))
                .collect(Collectors.toList());
    }

    // Get all courses-class for a given department
    public List<CourseDao> getCoursesInDepartment_Class(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No courses found in department with ID %d.", departmentId));
        }
        return courses.stream()
                .map(CourseDao::new)
                .collect(Collectors.toList());
    }

    // Get all courses for a collage
    public List<CourseDao> getAllCourses() {
        if (departmentRepository.count() == 0)
            throw new ResourceNotFoundException("No departments found.");

        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty())
            throw new ResourceNotFoundException("No courses found in the college.");

        return courses.stream()
                .map(c -> new CourseDao(c.getId(), c.getTitle(), c.getCreditHours(), c.getDepartment().getName()))
                .collect(Collectors.toList());
    }

    // Get all courses-class for a collage
    public List<CourseDao> getAllCourses_Class() {
        if (departmentRepository.count() == 0)
            throw new ResourceNotFoundException("No departments found.");

        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty())
            throw new ResourceNotFoundException("No courses found in the college.");

        return courses.stream()
                .map(CourseDao::new)
                .collect(Collectors.toList());
    }

    // Add new a course
    public boolean addCourse(Course course, Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (courseRepository.existsByTitle(course.getTitle())) {
            return false;
        } else {
            course.setDepartment(departmentRepository.findById(departmentId).get());
            courseRepository.save(course);
            return true;
        }
    }

    // Delete a course by their ID
    public boolean deleteCourseById(Integer departmentId, Integer id) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d.", id));

        if (courseRepository.existsByIdAndDepartmentId(id, departmentId)) {
            return false;
        } else {
            courseRepository.deleteById(id);
            return true;
        }
    }

    // Delete all courses in a given department
    public boolean deleteCoursesInDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty()) {
            return false;
        } else {
            courseRepository.deleteAll(courses);
            return true;
        }
    }

    // Delete all courses in a given collage
    public boolean deleteAllCourses() {
        if (courseRepository.count() == 0) {
            return false;
        } else {
            courseRepository.deleteAll();
            return true;
        }
    }

    // Update student details
    public boolean updateCourse(Course course, Integer departmentId, Integer id) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId)));

        Course updatingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Course not found with ID %d.", id)));

        if (courseRepository.existsByIdAndDepartmentId(id, departmentId))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d in %s department.", id, department.getName()));

        try {
            updatingCourse.setTitle(course.getTitle());
            updatingCourse.setCreditHours(course.getCreditHours());
            courseRepository.save(updatingCourse);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    // Add Prerequisite a course
    public boolean addPrerequisite(Integer departmentId, Integer id, Integer prerequisiteId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d.", id));

        if (courseRepository.existsByIdAndDepartmentId(id, departmentId)) {
            return false;
        } else {
            Course course = courseRepository.findById(id).get();
            course.addPrerequisite(courseRepository.findById(prerequisiteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prerequisite course not found with ID " + prerequisiteId)));
            courseRepository.save(course);
            return true;
        }
    }

    // Add Prerequisite a course
    public boolean deletePrerequisite(Integer departmentId, Integer id, Integer prerequisiteId) {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException(String.format("Department not found with ID %d.", departmentId));

        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException(String.format("Course not found with ID %d.", id));

        if (courseRepository.existsByIdAndDepartmentId(id, departmentId)) {
            return false;
        } else {
            Course course = courseRepository.findById(id).orElseThrow(null);
            Course prerequisite = courseRepository.findById(prerequisiteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prerequisite course not found with ID " + prerequisiteId));
            course.removePrerequisite(prerequisite);
            courseRepository.save(course);
            return true;
        }
    }
}