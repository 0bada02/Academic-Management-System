package ju.Repository;

import ju.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositry extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
