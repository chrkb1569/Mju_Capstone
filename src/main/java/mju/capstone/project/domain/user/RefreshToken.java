package mju.capstone.project.domain.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RefreshToken {
    @Id
    private String id;

    @Column(nullable = false)
    private String refreshToken;

    public void updateValue(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
