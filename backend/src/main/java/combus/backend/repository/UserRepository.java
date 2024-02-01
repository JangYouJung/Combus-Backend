package combus.backend.repository;
import combus.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findById(Long id);
}
