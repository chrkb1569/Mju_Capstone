package mju.capstone.project.controller.user;

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
    public void signUp(@RequestBody @Valid UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
    }

    @PostMapping("/user/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@RequestBody @Valid UserSignInRequestDto requestDto) {
        return Response.success(userService.signIn(requestDto));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Response reIssue(@RequestBody @Valid TokenReIssueDto reIssueDto) {
        return Response.success(userService.reissue(reIssueDto));
    }
}
