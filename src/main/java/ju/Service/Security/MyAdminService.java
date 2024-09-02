package ju.Service.Security;

import ju.Model.Admin.Admin;
import ju.Repository.AdminRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyAdminService implements UserDetailsService {

    @Autowired
    private AdminRepositry adminRepositry;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepositry.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new AdminPrincipal(admin);
    }
}