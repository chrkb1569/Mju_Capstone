package mju.capstone.project.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import mju.capstone.project.domain.authority.Authority;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.board.*;
import mju.capstone.project.repository.board.BoardRepository;
import mju.capstone.project.repository.user.UserRepository;
import mju.capstone.project.service.board.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(boardController)
                .build();
    }

    @Test
    @DisplayName(value = "게시글 전체 조회 테스트")
    public void getBoardsTest() throws Exception {
        //given
        Long categoryId = 1L;
        int page = 1;
        BoardWithPagingResponseDto responseDto = new BoardWithPagingResponseDto();

        //stub
        given(boardService.findBoards(categoryId, page)).willReturn(responseDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/boards/all/" + categoryId + "?page=" + page))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).findBoards(categoryId, page);
    }

    @Test
    @DisplayName(value = "최근 게시글 조회 테스트")
    public void getRecentBoardsTest() throws Exception {
        //given
        PageRequest page = PageRequest.of(0, 16, Sort.by("createdDate").descending());

        //when
        Page<Board> all = boardRepository.findAll(page);

        //then
        Assertions.assertThat(all).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "분실물 이름을 통한 게시글 검색 테스트 - 최신순")
    public void searchBoardByItemTest() {
        //given
        String itemName = "test";
        PageRequest request = PageRequest.of(0, 9, Sort.by("createdDate").descending());

        //when
        Page<Board> result = boardRepository.findAllByItemNameContaining(itemName, request);

        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "분실물 이름을 통한 게시글 검색 테스트 - 조회순")
    public void searchBoardByItem_viewCountTest() {
        //given
        String itemName = "test";
        PageRequest request = PageRequest.of(0, 9, Sort.by("viewCount").descending());

        //when
        Page<Board> result = boardRepository.findAllByItemNameContaining(itemName, request);

        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "게시글 제목을 통한 게시글 조회 테스트 - 최신순")
    public void searchBoardByTitleTest() {
        //given
        String title = "test";
        PageRequest request = PageRequest.of(0, 9, Sort.by("createdDate").descending());

        //when
        Page<Board> result = boardRepository.findAllByTitleContaining(title, request);

        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "게시글 제목을 통한 게시글 조회 테스트 - 조회순")
    public void searchBoardByTitle_viewCountTest() {
        //given
        String title = "test";
        PageRequest request = PageRequest.of(0, 9, Sort.by("viewCount").descending());

        //when
        Page<Board> result = boardRepository.findAllByTitleContaining(title, request);

        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName(value = "게시글 단일 조회 테스트")
    public void getBoardTest() throws Exception {
        //given
        Long id = 1L;
        BoardDetailedDto detailedDto = new BoardDetailedDto();

        //stub
        given(boardService.findBoard(id)).willReturn(detailedDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/board/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).findBoard(id);
    }

    @Test
    @DisplayName(value = "일련 번호를 통한 게시글 조회 테스트")
    public void getBoardBySerialNumberTest() throws Exception {
        //given
        String serial = "123-456";
        BoardResponseDto responseDto = new BoardResponseDto();

        //stub
        given(boardService.findBoardBySerialNumber(serial)).willReturn(responseDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/board/serial/" + serial))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).findBoardBySerialNumber(serial);
    }

    @Test
    @DisplayName(value = "게시글에 게시된 분실물 확인 테스트")
    public void checkItemTest() throws Exception {
        //given
        Long id = 1L;
        String serial = "123-456";

        //stub
        given(boardService.checkItem(id, serial)).willReturn(true);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/board/" + id + "/" + serial))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).checkItem(id, serial);
    }

    @Test
    @DisplayName(value = "QR코드 생성 테스트")
    public void getQRCodeTest() throws Exception {
        //given
        String serial = "serial";
        String result = "result";

        //stub
        given(boardService.getQRCode(serial)).willReturn(result);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/board/QR/" + serial))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).getQRCode(serial);
    }

    @Test
    @DisplayName(value = "게시글 생성 테스트")
    public void makeBoardTest() throws Exception {
        //given
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("test1", "test1.jpeg", MediaType.IMAGE_JPEG.toString(), "test1".getBytes()));
        files.add(new MockMultipartFile("test2", "test2.jpeg", MediaType.IMAGE_JPEG.toString(), "test2".getBytes()));
        files.add(new MockMultipartFile("test3", "test3.jpeg", MediaType.IMAGE_JPEG.toString(), "test3".getBytes()));
        BoardCreateDto createDto =
                new BoardCreateDto("title", "content", "itemName", "serialNumber",
                        12.34, 34.56, files);
        BoardDetailedDto detailedDto = new BoardDetailedDto();
        Long categoryId = 1L;
        Long subCategoryId = 1L;
        Long leafCategoryId = 1L;

        User user = new User(1L, "username", "name", "nickname",
                "password", "email", "provider", Authority.ROLE_USER);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), "", null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //stub
        given(userRepository.findUserByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(boardService.makeBoard(any(BoardCreateDto.class), any(User.class), anyLong(), anyLong(), anyLong())).willReturn(detailedDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/boards?mainCategory=" + categoryId + "&subCategory=" + subCategoryId + "&leafCategory=" + leafCategoryId)
                .file("files", files.get(0).getBytes())
                .file("files", files.get(1).getBytes())
                .file("files", files.get(2).getBytes())
                .param("title", createDto.getTitle())
                .param("content", createDto.getContent())
                .param("itemName", createDto.getItemName())
                .param("serialNumber", createDto.getSerialNumber())
                .param("latitude", "" + createDto.getLatitude())
                .param("longitude", "" + createDto.getLongitude())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //then
        verify(boardService).makeBoard(any(BoardCreateDto.class), any(User.class), anyLong(), anyLong(), anyLong());
    }

    @Test
    @DisplayName(value = "게시글 수정 테스트")
    public void editBoardTest() throws Exception {
        //given
        List<MultipartFile> addImage = new ArrayList<>();
        addImage.add(new MockMultipartFile("test1", "test1.jpeg", MediaType.IMAGE_JPEG.toString(), "test1".getBytes()));

        List<Long> deleteImage = new ArrayList<>();
        deleteImage.add(1L);

        BoardEditRequestDto requestDto =
                new BoardEditRequestDto("title", "content", addImage, deleteImage);

        Long id = 1L;

        User user = new User(1L, "username", "name", "nickname",
                "password", "email", "provider", Authority.ROLE_USER);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), "", null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //stub
        willDoNothing().given(boardService).editBoard(any(BoardEditRequestDto.class), any(User.class), anyLong());
        given(userRepository.findUserByUsername(user.getUsername())).willReturn(Optional.of(user));

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/board/" + id)
                .file("addImage", addImage.get(0).getBytes())
                .param("title", requestDto.getTitle())
                .param("content", requestDto.getContent())
                .param("deleteImage", "" + requestDto.getDeleteImage().get(0))
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).editBoard(any(BoardEditRequestDto.class), any(User.class), anyLong());
    }

    @Test
    @DisplayName(value = "게시글 삭제 테스트")
    public void deleteBoardTest() throws Exception {
        //given
        User user = new User(1L, "username", "name", "nickname", "password",
                "email", "provider", Authority.ROLE_USER);
        Long id = 1L;

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), "", null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //stub
        willDoNothing().given(boardService).deleteBoard(user, id);
        given(userRepository.findUserByUsername(any())).willReturn(Optional.of(user));

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/board/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(boardService).deleteBoard(user, id);
    }
}
