package mju.capstone.project.service.user;

import mju.capstone.project.config.jwt.TokenProvider;
import mju.capstone.project.domain.authority.Authority;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.user.UserSignInRequestDto;
import mju.capstone.project.dto.user.UserSignUpRequestDto;
import mju.capstone.project.exception.user.LoginFailureException;
import mju.capstone.project.repository.refreshToken.RefreshTokenRepository;
import mju.capstone.project.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName(value = "signUp() - 회원가입 테스트")
    public void signUpTest() {
        //given
        UserSignUpRequestDto requestDto = new UserSignUpRequestDto("username", "name", "nickname", "password", "email");

        //stub
        given(userRepository.existsUserByUsername("username")).willReturn(false);
        given(userRepository.existsUserByNickname("nickname")).willReturn(false);
        given(userRepository.save(any())).willReturn(any());

        //when
        userService.signUp(requestDto);

        //then
        verify(userRepository).existsUserByNickname(requestDto.getNickname());
        verify(userRepository).existsUserByUsername(requestDto.getUsername());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName(value = "signIn() - 로그인 테스트")
    public void signInTest() {
        //given
        UserSignInRequestDto requestDto = new UserSignInRequestDto("username", "password");
        User user = new User(1L, "username", "name", "nickname", "password", "email", "provider", Authority.ROLE_USER);

        //stub
        given(userRepository.findUserByUsername(any())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(user.getPassword(), requestDto.getPassword())).willReturn(false);

        //when, then
        Assertions.assertThrows(LoginFailureException.class, () -> {
            userService.signIn(requestDto);
        });
    }
}
