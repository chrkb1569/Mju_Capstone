package mju.capstone.project.repository.refreshToken;

import mju.capstone.project.domain.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
