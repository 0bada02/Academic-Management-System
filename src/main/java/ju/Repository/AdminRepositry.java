package ju.Repository;

import ju.Model.Admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepositry extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}
