package ju.service;

import ju.repository.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private UserRepositry userRepositry;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositry.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}