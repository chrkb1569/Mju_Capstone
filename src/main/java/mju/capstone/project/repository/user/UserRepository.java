package mju.capstone.project.repository.user;

import mju.capstone.project.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByName(String name);
    boolean existsUserByEmailAndProvider(String email, String provider);
    boolean existsUserByUsername(String username);
    boolean existsUserByNickname(String nickname);
}
