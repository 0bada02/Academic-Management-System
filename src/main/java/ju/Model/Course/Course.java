package ju.Model.Course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ju.Exception.ResourceNotFoundException;
import ju.Model.Class.Class;
import ju.Model.Department.Department;
import lombok.*;

import java.util.*;

// Lombok's annotations for boilerplate code
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    @Column(unique = true, nullable = false)
    private String title;

    @NotNull(message = "Credit Hours cannot be null")
    @Column(nullable = false)
    private Integer creditHours;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Prerequisite_Courses",
            joinColumns = @JoinColumn(name = "Course_Id"),
            inverseJoinColumns = @JoinColumn(name = "Prerequisite_Id")
    )
    private List<Course> prerequisites = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Department_Id")
    private Department department;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Class> classes = new ArrayList<>();

    public void addPrerequisite(Course prerequisite) {
        if (this.getId().equals(prerequisite.getId()))
            throw new ResourceNotFoundException("A course cannot be a prerequisite of itself.");

        boolean prerequisiteExists = prerequisites.stream()
                .anyMatch(prereq -> prereq.getId().equals(prerequisite.getId()));
        if (prerequisiteExists)
            throw new ResourceNotFoundException(String.format("The '%s' course is already a prerequisite.", prerequisite.getTitle()));
        if (isPrerequisiteOf(this, prerequisite))
            throw new ResourceNotFoundException("Adding this prerequisite would create a circular dependency.");
        prerequisites.add(prerequisite);
    }

    private boolean isPrerequisiteOf(Course currentCourse, Course prerequisite) {
        return prerequisite.getPrerequisites().stream()
                .anyMatch(prereq -> prereq.getId().equals(currentCourse.getId()) || isPrerequisiteOf(currentCourse, prereq));
    }

    public void removePrerequisite(Course prerequisite) {
        if (prerequisites.isEmpty())
            throw new ResourceNotFoundException("Cannot remove prerequisite: No prerequisites are currently set.");

        boolean removed = prerequisites.removeIf(prereq -> prereq.getId().equals(prerequisite.getId()));
        if (!removed)
            throw new ResourceNotFoundException(String.format("Cannot remove prerequisite: The course '%s' is not a prerequisite.", prerequisite.getTitle()));
    }
}