package mju.capstone.project.service.board;

import mju.capstone.project.domain.authority.Authority;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.board.BoardCreateDto;
import mju.capstone.project.dto.board.BoardEditRequestDto;
import mju.capstone.project.repository.board.BoardRepository;
import mju.capstone.project.repository.category.CategoryRepository;
import mju.capstone.project.repository.category.LeafCategoryRepository;
import mju.capstone.project.repository.category.SubCategoryRepository;
import mju.capstone.project.service.image.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private LeafCategoryRepository leafCategoryRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private BoardService boardService;

    public static List<Board> boardList;
    public static Board board1;
    public static Board board2;
    public static Category category;
    public static SubCategory subCategory;
    public static LeafCategory leafCategory;

    @BeforeEach
    public void init() {
        category = new Category(1L, "category");
        subCategory = new SubCategory(1L, "subCategory", category);
        leafCategory = new LeafCategory(1L, "leafCategory", subCategory);

        board1 = new Board(1L, "Test Title1", "Test Content1", "Test Writer", 0,
                "Test Item", "123-123-123", 12.34,
                56.78, category, subCategory, leafCategory, new ArrayList<>());
        board2 = new Board(2L, "Test Title2", "Test Content2", "Test Writer", 0,
                "Test Item", "123-123-123", 12.34,
                56.78, category, subCategory, leafCategory, new ArrayList<>());

        boardList = new ArrayList<>();
        boardList.add(board1);
        boardList.add(board2);
    }

    @Test
    @DisplayName(value = "findBoards() - 특정 카테고리에 게시글 검색 테스트")
    public void findBoardsTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        Page<Board> boards = new PageImpl<>(boardList);

        //stub
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(boardRepository.findAllByCategory(category, pageRequest)).willReturn(boards);

        //when
        boardService.findBoards(category.getId(), 0);

        //then
        verify(categoryRepository).findById(any());
        verify(boardRepository).findAllByCategory(category, pageRequest);
    }

    @Test
    @DisplayName(value = "findNewBoards() - 최근에 생성된 게시글 검색 테스트")
    public void findNewBoardsTest() {
        //given
        PageRequest request = PageRequest.of(0, 16, Sort.by("createdDate").descending());

        Page<Board> page = new PageImpl<>(boardList);

        //stub
        given(boardRepository.findAll(request)).willReturn(page);

        //when
        boardService.findNewBoards(request);

        //then
        verify(boardRepository).findAll(request);
    }

    @Test
    @DisplayName(value = "searchBoardByItem() - 분실물의 이름을 통한 검색 테스트")
    public void searchBoardByItemTest() {
        //given
        PageRequest request = PageRequest.of(0, 9, Sort.by("CreatedDate").descending());
        Page<Board> page = new PageImpl<>(boardList);

        //stub
        given(boardRepository.findAllByItemNameContaining("Test Item", request)).willReturn(page);

        //when
        boardService.searchBoardByItem("Test Item", 0, request);

        //then
        verify(boardRepository).findAllByItemNameContaining("Test Item", request);
    }

    @Test
    @DisplayName(value = "searchBoardByTitle() - 게시글 제목을 통한 검색 테스트")
    public void searchBoardByTitleTest() {
        //given
        PageRequest request = PageRequest.of(0, 9, Sort.by("createdDate").descending());
        Page<Board> page = new PageImpl<>(boardList);

        //stub
        given(boardRepository.findAllByTitleContaining("Test Title1", request)).willReturn(page);

        //when
        boardService.searchBoardByTitle("Test Title1", 0, request);

        //then
        verify(boardRepository).findAllByTitleContaining("Test Title1", request);
    }

    @Test
    @DisplayName(value = "findBoard() - 단일 게시글 검색 테스트")
    public void findBoardTest() {
        //given
        int viewCount = board1.getViewCount();

        //stub
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));

        //when
        boardService.findBoard(anyLong());

        //then
        Assertions.assertThat(viewCount).isEqualTo(board1.getViewCount()-1);
        verify(boardRepository).findById(anyLong());
    }

    @Test
    @DisplayName(value = "findBoardBySerialNumber() - 일련 번호를 통한 게시글 검색 테스트")
    public void findBoardBySerialNumberTest() {
        //given

        //stub
        given(boardRepository.findBoardBySerialNumber(any())).willReturn(Optional.of(board1));

        //when
        boardService.findBoardBySerialNumber(any());

        //then
        verify(boardRepository).findBoardBySerialNumber(any());
    }

    @Test
    @DisplayName(value = "checkItem() - 일련 번호를 통한 물건 확인 테스트")
    public void checkItemTest() {
        //given

        //stub
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));

        //when
        boardService.checkItem(anyLong(), "123-123-123");

        //then
        verify(boardRepository).findById(any());
    }

    @Test
    @DisplayName(value = "makeBoard() - 게시글을 새롭게 생성하는 테스트")
    public void makeBoardTest() {
        //given
        List<MultipartFile> files = new ArrayList<>();
        MultipartFile image1 =new MockMultipartFile("test1", "test1.jpeg", MediaType.IMAGE_JPEG.toString(), "test1".getBytes());
        MultipartFile image2 = new MockMultipartFile("test2", "test2.jpeg", MediaType.IMAGE_JPEG.toString(), "test2".getBytes());
        MultipartFile image3 = new MockMultipartFile("test3", "test3.jpeg", MediaType.IMAGE_JPEG.toString(), "test3".getBytes());

        files.add(image1);
        files.add(image2);
        files.add(image3);

        BoardCreateDto createDto = new BoardCreateDto("Title", "Content", "ItemName", "123-456-789", 12.34, 34.56, files);

        User user = new User(1L, "Test User", "user", "test", "1234", "test@test.com", "google", Authority.ROLE_USER);

        //stub
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));
        given(leafCategoryRepository.findById(anyLong())).willReturn(Optional.of(leafCategory));
        given(boardRepository.save(any())).willReturn(board1);

        //when
        boardService.makeBoard(createDto, user, category.getId(), subCategory.getId(), leafCategory.getId());

        //then
        verify(categoryRepository).findById(anyLong());
        verify(subCategoryRepository).findById(anyLong());
        verify(leafCategoryRepository).findById(anyLong());
        verify(boardRepository).save(any());
    }

    @Test
    @DisplayName(value = "getQRCode() - QR코드를 새롭게 생성하는 테스트")
    public void getQRCodeTest() {
        //given
        String serial = "LOST-testSerial";

        //stub
        given(boardRepository.findBoardBySerialNumber(serial)).willReturn(Optional.of(board1));

        //when
        String url = boardService.getQRCode(serial);

        //then
        Assertions.assertThat(url.contains(String.valueOf(board1.getId()))).isTrue();
        verify(boardRepository).findBoardBySerialNumber(serial);
    }

    @Test
    @DisplayName(value = "editBoard() - 게시글을 수정하는 테스트")
    public void editBoardTest() {
        //given
        List<MultipartFile> files = new ArrayList<>();
        MultipartFile image1 =new MockMultipartFile("test1", "test1.jpeg", MediaType.IMAGE_JPEG.toString(), "test1".getBytes());
        MultipartFile image2 = new MockMultipartFile("test2", "test2.jpeg", MediaType.IMAGE_JPEG.toString(), "test2".getBytes());
        MultipartFile image3 = new MockMultipartFile("test3", "test3.jpeg", MediaType.IMAGE_JPEG.toString(), "test3".getBytes());

        files.add(image1);
        files.add(image2);
        files.add(image3);

        List<Long> delete = new ArrayList<>();
        delete.add(1L);
        delete.add(2L);
        delete.add(3L);

        BoardEditRequestDto requestDto = new BoardEditRequestDto("Title", "Content", files, delete);
        User user = new User(1L, "Test Writer", "user", "test", "1234", "test@test.com", "google", Authority.ROLE_USER);

        //stub
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));

        //when
        boardService.editBoard(requestDto, user, anyLong());

        //then
        verify(boardRepository).findById(anyLong());
        Assertions.assertThat(board1.getContent()).isEqualTo("Content");
    }

    @Test
    @DisplayName(value = "deleteBoard() - 게시글을 삭제하는 테스트")
    public void deleteBoardTest() {
        //given
        User user = new User(1L, "Test Writer", "user", "test", "1234", "test@test.com", "google", Authority.ROLE_USER);

        //stub
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board1));
        willDoNothing().given(boardRepository).deleteById(anyLong());

        //when
        boardService.deleteBoard(user, anyLong());

        //then
        verify(boardRepository).findById(anyLong());
        verify(boardRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName(value = "checkUserValidation() - 사용자와 작성자의 일치 확인 테스트")
    public void checkUserValidationTest() {
        //given
        User user = new User(1L, "Other Writer", "user", "test", "1234", "test@test.com", "google", Authority.ROLE_USER);

        //stub

        //when
        boolean result = boardService.checkUserValidation(board1, user);

        //then
        Assertions.assertThat(result).isFalse();
    }
}
