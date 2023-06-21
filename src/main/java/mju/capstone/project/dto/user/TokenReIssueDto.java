package mju.capstone.project.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenReIssueDto {

    @NotBlank(message = "토큰 재발급을 위한 access 토큰 값을 입력해주세요.")
    @ApiModelProperty(value = "토큰 재발급을 위한 accessToken", example = "accessToken")
    private String accessToken;

    @NotBlank(message = "토큰 재발급을 위한 refresh 토큰 값을 입력해주세요")
    @ApiModelProperty(value = "토큰 재발급을 위한 refreshToken", example = "refreshToken")
    private String refreshToken;
}
