package mju.capstone.project.controller.user;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.dto.user.TokenReIssueDto;
import mju.capstone.project.dto.user.UserSignInRequestDto;
import mju.capstone.project.dto.user.UserSignUpRequestDto;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "회원 가입", notes = "신규 회원으로 가입하기 위한 로직")
    @ApiImplicitParam(name = "requestDto", value = "회원가입 정보를 담은 DTO")
    public void signUp(@RequestBody @Valid UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
    }

    @PostMapping("/user/sign-in")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인", notes = "로그인을 위한 로직")
    @ApiImplicitParam(name = "requestDto", value = "로그인을 위한 정보를 담은 DTO")
    public Response signIn(@RequestBody @Valid UserSignInRequestDto requestDto) {
        return Response.success(userService.signIn(requestDto));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "토큰 재발급", notes = "JWT 토큰을 다시 발급받기 위한 로직")
    @ApiImplicitParam(name = "reIssueDto", value = "토큰을 재발급에 필요한 정보가 담긴 DTO")
    public Response reIssue(@RequestBody @Valid TokenReIssueDto reIssueDto) {
        return Response.success(userService.reissue(reIssueDto));
    }
}
