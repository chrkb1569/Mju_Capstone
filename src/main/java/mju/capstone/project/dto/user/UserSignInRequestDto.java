package mju.capstone.project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignInRequestDto {

    @NotBlank(message = "로그인을 위한 아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "로그인을 위한 비밀번호를 입력해주세요.")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.username, this.password);
    }

}
