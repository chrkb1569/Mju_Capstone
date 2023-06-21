package mju.capstone.project.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import mju.capstone.project.dto.user.UserSignInRequestDto;
import mju.capstone.project.dto.user.UserSignUpRequestDto;
import mju.capstone.project.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    @DisplayName(value = "회원 가입 테스트")
    public void signUpTest() throws Exception {
        //given
        UserSignUpRequestDto requestDto =
                new UserSignUpRequestDto("username", "name", "nickname", "asdf1234!@#$", "email@email.com");

        //stub
        willDoNothing().given(userService).signUp(any());

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //then
        verify(userService).signUp(any());
    }

    @Test
    @DisplayName(value = "로그인 테스트")
    public void signInTest() throws Exception {
        //given
        UserSignInRequestDto requestDto = new UserSignInRequestDto("username", "password");

        //stub
        given(userService.signIn(any())).willReturn(any());

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(userService).signIn(any());
    }
}
