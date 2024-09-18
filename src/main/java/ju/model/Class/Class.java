package ju.model.Class;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ju.exception.ResourceNotFoundException;
import ju.model.Class.Enum.ClassStatus;
import ju.model.Class.Enum.DaysOfWeek.DaysOfWeek;
import ju.model.Class.Enum.DaysOfWeek.DaysOfWeekConverter;
import ju.model.Class.Enum.Semester;
import ju.model.ClassStudent.Enum.Passed;
import ju.model.ClassStudent.ClassStudent;
import ju.model.Course;
import ju.model.Instructor;
import ju.model.Student;
import lombok.*;

import java.sql.Time;
import java.time.Year;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Classes")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Time startTime;

    private Time endTime;

    @Convert(converter = DaysOfWeekConverter.class)
    private List<DaysOfWeek> daysOfWeek;

    private String roomNumber;

    private Integer capacity;

    @NotNull(message = "Semester Required cannot be null")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @NotNull(message = "Year Required cannot be null")
    @Column(nullable = false)
    private Year year;

    @Enumerated(EnumType.STRING)
    private ClassStatus status = ClassStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "Course_Id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "Instructor_Id")
    private Instructor instructor;

    @OneToMany(mappedBy = "aClass", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ClassStudent> classStudents = new ArrayList<>();

    public void addStudent(Student student, Double grade) {
        classStudents.stream()
                .filter(cs -> cs.getStudent().getId().equals(student.getId()))
                .findFirst()
                .ifPresent(cs -> {
                    if (cs.getPassed().equals(Passed.PASS)) {
                        throw new ResourceNotFoundException("Student has finished this class.");
                    } else if (cs.getPassed().equals(Passed.ACTIVE)) {
                        throw new ResourceNotFoundException("Student is already enrolled in this class.");
                    } else
                        throw new ResourceNotFoundException("Student is failed in this class.");
                });

        student.getClassStudents().stream()
                .filter(s -> s.getAClass().getCourse().getId().equals(this.getCourse().getId()))
                .findFirst()
                .ifPresent(s -> {
                    if (s.getPassed().equals(Passed.PASS)) {
                        throw new ResourceNotFoundException("Student has already passed this course.");
                    } else if (s.getPassed().equals(Passed.ACTIVE)) {
                        throw new ResourceNotFoundException("Student is already enrolled in this course.");
                    } else
                        throw new ResourceNotFoundException("Student is failed in this course.");
                });

        // Check if a student is enrolled in another class at the same time
        for (ClassStudent s : student.getClassStudents())
            if (isTimeOverlapping(this, s.getAClass()))
                throw new ResourceNotFoundException("Student is already enrolled in another class at the same time.");

        if (classStudents.size() >= capacity)
            throw new ResourceNotFoundException("Cannot add student: Maximum capacity reached.");

        if (!hasCompletedPrerequisites(this.getCourse(), student))
            throw new ResourceNotFoundException("Cannot add student: Prerequisites not completed.");

        createClassStudent(student, grade);
    }

    public void removeStudent(Student student) {
        ClassStudent classStudent = classStudents.stream()
                .filter(cs -> cs.getStudent().getId().equals(student.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cannot remove student: Student is not enrolled in this class."));

        classStudents.remove(classStudent);
    }

    public void addInstructor(Instructor instructor) {

        for (Class aClass : instructor.getClasses())
            if (isTimeOverlapping(this, aClass))
                throw new ResourceNotFoundException("Instructor is already assigned in another class at the same time.");

        if (this.instructor == null)
            this.instructor = instructor;
        else
            throw new ResourceNotFoundException("Cannot add instructor: An instructor is already assigned to this class.");
    }

    public void changeInstructor(Instructor instructor) {
        if (this.instructor == null)
            throw new ResourceNotFoundException("Cannot change instructor: No instructor is currently assigned to this class.");
        else
            this.instructor = instructor;
    }

    private boolean hasCompletedCourse(Course course, Student student) {
        return student.getClassStudents().stream()
                .filter(cs -> cs.getAClass().getCourse().getId().equals(course.getId()))
                .anyMatch(cs -> cs.getPassed().equals(Passed.PASS));
    }

    public boolean hasCompletedPrerequisites(Course course, Student student) {
        List<Course> prerequisites = course.getPrerequisites();

        for (Course prerequisite : prerequisites) {
            if (!hasCompletedCourse(prerequisite, student)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTimeOverlapping(Class thisClass, Class otherClass) {
        if (thisClass.getYear().equals(otherClass.getYear()) &&
                thisClass.getSemester().equals(otherClass.getSemester()) &&
                Collections.disjoint(thisClass.getDaysOfWeek(), otherClass.getDaysOfWeek())) {

            return thisClass.getStartTime().before(otherClass.getEndTime()) &&
                    thisClass.getEndTime().after(otherClass.getStartTime());
        }
        return false;
    }

    public void createClassStudent(Student student, Double grade) {
        ClassStudent classStudent = new ClassStudent();
        classStudent.setAClass(this);
        classStudent.setStudent(student);
        Integer courseCreditHours = classStudent.getAClass().getCourse().getCreditHours();

        if (grade == null)
            classStudent.setPassed(Passed.ACTIVE);
        else {
            classStudent.setGrade(grade);
            classStudent.setPassed(grade >= 45 ? Passed.PASS : Passed.FAILED);
            classStudent.setLetterGrades(convertToLetter(grade));
            student.updateGPA(classStudent);
            if (grade >= 45) {
                student.setTotalHoursCompleted(student.getTotalHoursCompleted() + courseCreditHours);
                student.setTotalHoursRemaining(student.getDepartment().getTotalHoursRequired() - student.getTotalHoursCompleted());
            } else {
                student.setTotalHoursFailed(student.getTotalHoursFailed() + courseCreditHours);
            }
        }
        classStudents.add(classStudent);
        this.setStatus(this.getClassStudents().size() >= this.getCapacity() ? ClassStatus.CLOSE : ClassStatus.OPEN);
    }

    public static String convertToLetter(Double numericGrade) {
        if (numericGrade >= 95) {
            return "A";
        } else if (numericGrade >= 90) {
            return "A-";
        } else if (numericGrade >= 85) {
            return "B+";
        } else if (numericGrade >= 80) {
            return "B";
        } else if (numericGrade >= 75) {
            return "B-";
        } else if (numericGrade >= 70) {
            return "C+";
        } else if (numericGrade >= 65) {
            return "C";
        } else if (numericGrade >= 60) {
            return "C-";
        } else if (numericGrade >= 55) {
            return "D+";
        } else if (numericGrade >= 45) {
            return "D";
        } else if (numericGrade >= 40) {
            return "D-";
        } else {
            return "F";
        }
    }

    public static Double convertToGPA(String letterGrade) {
        return switch (letterGrade) {
            case "A" -> 4.0;
            case "A-" -> 3.75;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "B-" -> 2.75;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "C-" -> 1.75;
            case "D+" -> 1.5;
            case "D" -> 1.0;
            case "D-" -> 0.75;
            case "F" -> 0.0;
            default -> throw new IllegalArgumentException("Invalid letter grade: " + letterGrade);
        };
    }
}