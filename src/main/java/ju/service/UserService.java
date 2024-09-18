package ju.service;

import jakarta.transaction.Transactional;
import ju.model.Instructor;
import ju.model.Role;
import ju.model.Student;
import ju.model.User;
import ju.repository.InstructorRepository;
import ju.repository.StudentRepository;
import ju.repository.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StudentRepository studentRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    @Autowired
    private InstructorRepository instructorRepository;

    public String login(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated())
            return jwtService.generateJWT(user);

        return null;
    }

    @Transactional
    public void studentRegister(User user, Integer studentId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);
        Student student = studentRepository.findById(studentId).get();
        student.setUser(user);
        userRepositry.save(user);
        studentRepository.save(student);
    }

    public void instructorRegister(User user, Integer instructorId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.INSTRUCTOR);
        Instructor instructor = instructorRepository.findById(instructorId).get();
        instructor.setUser(user);
        userRepositry.save(user);
        instructorRepository.save(instructor);
    }

//    public void studentRegistration(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepositry.save(user);
//    }
}