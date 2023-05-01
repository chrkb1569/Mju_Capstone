package mju.capstone.project.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.dto.user.TokenResponseDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TokenDto {

    private String originToken;
    private String refreshToken;
    private String type;
    private Long validateTime;

    public TokenResponseDto toDto() {
        return TokenResponseDto.builder()
                .accessToken(this.originToken)
                .refreshToken(this.refreshToken)
                .build();
    }
}
