package ju.Controller;

import ju.Model.Admin.Admin;
import ju.Service.Security.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addAdmin")
    public void addAdmin(@RequestBody Admin admin) {
        adminService.addAdmin(admin);
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin admin) {
        return adminService.verify(admin);
    }
}