package mju.capstone.project.service.user;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.config.jwt.TokenProvider;
import mju.capstone.project.domain.authority.Authority;
import mju.capstone.project.domain.user.RefreshToken;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.token.TokenDto;
import mju.capstone.project.dto.user.TokenReIssueDto;
import mju.capstone.project.dto.user.TokenResponseDto;
import mju.capstone.project.dto.user.UserSignInRequestDto;
import mju.capstone.project.dto.user.UserSignUpRequestDto;
import mju.capstone.project.exception.refreshToken.RefreshTokenNotFoundException;
import mju.capstone.project.exception.user.LoginFailureException;
import mju.capstone.project.exception.user.UserDuplicateException;
import mju.capstone.project.repository.refreshToken.RefreshTokenRepository;
import mju.capstone.project.repository.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    @Transactional
    public void signUp(UserSignUpRequestDto requestDto) {
        userDuplicateTest(requestDto);
        User user = makeUser(requestDto);
        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public TokenResponseDto signIn(UserSignInRequestDto requestDto) {
        User user = userValidateTest(requestDto.getUsername(), requestDto.getPassword());
        UsernamePasswordAuthenticationToken tokenValue = requestDto.getAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(tokenValue);

        TokenDto createdToken = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(user.getUsername())
                .refreshToken(createdToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return createdToken.toDto();
    }

    //토큰 재발급
    @Transactional
    public TokenResponseDto reissue(TokenReIssueDto reIssueDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        RefreshToken findToken = refreshTokenRepository.findById(username)
                .orElseThrow(RefreshTokenNotFoundException::new);

        refreshTokenValidateTest(findToken, reIssueDto.getRefreshToken());

        TokenDto createdToken = tokenProvider.createToken(authentication);

        findToken.updateValue(createdToken.getRefreshToken());

        return createdToken.toDto();
    }

    public void refreshTokenValidateTest(RefreshToken findToken, String refreshToken) {
        if(!findToken.getRefreshToken().equals(refreshToken))
            throw new RefreshTokenNotFoundException();
    }

    public User userValidateTest(String username, String password) {
        User findUser = userRepository.findUserByUsername(username).orElseThrow(LoginFailureException::new);

        if(!passwordEncoder.matches(password, findUser.getPassword()))
            throw new LoginFailureException();

        return findUser;
    }

    public void userDuplicateTest(UserSignUpRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        String username = requestDto.getUsername();

        if(userRepository.existsUserByNickname(nickname))
            throw new UserDuplicateException(nickname + "은 이미 사용중인 별명입니다. 다른 별명을 사용해주세요.");

        if(userRepository.existsUserByUsername(username))
            throw new UserDuplicateException(username + "은 이미 사용중인 아이디입니다. 다른 아이디를 사용해주세요.");
    }

    public User makeUser(UserSignUpRequestDto requestDto) {
        return User.builder()
                .username(requestDto.getUsername())
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .authority(Authority.ROLE_USER)
                .build();
    }
}
