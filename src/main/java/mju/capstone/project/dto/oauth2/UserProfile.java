package mju.capstone.project.dto.oauth2;

import lombok.Getter;
import lombok.Setter;
import mju.capstone.project.domain.authority.Authority;
import mju.capstone.project.domain.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Getter
@Setter
public class UserProfile {
    private String name;
    private String provider;
    private String email;

    public User makeUser() {
        String generatedUsername = provider + "-" + name;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return User.builder()
                .name(this.name)
                .email(this.email)
                .provider(this.provider)
                .username(generatedUsername)
                .nickname(provider + UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 4))
                .authority(Authority.ROLE_USER)
                .password(passwordEncoder.encode(this.email))
                .build();
    }
}
