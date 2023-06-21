package mju.capstone.project.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignUpRequestDto {

    @NotBlank(message = "회원가입을 위하여 사용자 아이디를 입력해주세요.")
    @Length(min = 5, max = 20, message = "최소 5자 이상, 최대 20자 미만의 아이디를 입력해주세요")
    @ApiModelProperty(value = "회원가입을 위한 사용자의 아이디, 중복된 아이디가 존재하는 경우 오류 반환", example = "test1234")
    private String username;

    @NotBlank(message = "회원가입을 위하여 사용자 이름을 입력해주세요.")
    @ApiModelProperty(value = "회원가입을 위한 사용자의 이름", example = "test")
    private String name;

    @NotBlank(message = "회원가입을 위하여 사용자 별명을 입력해주세요.")
    @ApiModelProperty(value = "회원가입을 위한 사용자의 별명, 중복된 별명이 존재하는 경우 오류 반환", example = "test")
    private String nickname;

    @NotBlank(message = "회원 가입을 위하여 사용자 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 영문과 특수문자, 숫자를 포함하여 8자 이상이어야합니다.")
    @ApiModelProperty(value = "회원가입을 위한 사용자 비밀번호", example = "1234")
    private String password;

    @NotBlank(message = "회원 가입을 위하여 사용자 이메일을 입력해주세요.")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
            message = "이메일 형식을 다시 한 번 확인해주세요")
    @ApiModelProperty(value = "회원가입을 위한 사용자 이메일, 중복되는 이메일이 존재하는 경우 오류 반환", example = "test123@test.com")
    private String email;
}
