package mju.capstone.project.dto.user;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "로그인을 위한 사용자의 아이디", example = "test1234")
    private String username;

    @NotBlank(message = "로그인을 위한 비밀번호를 입력해주세요.")
    @ApiModelProperty(value = "로그인을 위한 사용자의 비밀번호", example = "1234")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.username, this.password);
    }

}
