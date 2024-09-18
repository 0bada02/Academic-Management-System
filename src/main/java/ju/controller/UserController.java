package ju.controller;

import ju.model.User;
import ju.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/student-register/{studentId}")
    public void studentRegister(@RequestBody User user, @PathVariable Integer studentId) {
        userService.studentRegister(user, studentId);
    }

    @PostMapping("/instructor-register/{instructorId}")
    public void instructorRegister(@RequestBody User user, @PathVariable Integer instructorId) {
        userService.instructorRegister(user, instructorId);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user);
    }
}