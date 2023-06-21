package mju.capstone.project.controller.board;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.dto.board.BoardCreateDto;
import mju.capstone.project.dto.board.BoardEditRequestDto;
import mju.capstone.project.exception.user.UserNotFoundException;
import mju.capstone.project.repository.user.UserRepository;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.board.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    // 게시글 전체 조회 - 최신순
    @GetMapping("/boards/all/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 전체 조회", notes = "특정 카테고리에 해당하는 모든 게시글들을 조회하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "조회할 카테고리의 id", example = "1"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지의 번호, 기존값은 0으로 설정됨", example = "0")
    })
    public Response getBoards(@PathVariable Long categoryId, @RequestParam(defaultValue = "0") int page) {
        return Response.success(boardService.findBoards(categoryId, page));
    }

    //게시글 전체 조회 - 최신글 16개만 조회
    @GetMapping("/boards/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "최신 게시글 조회", notes = "최근에 작성된 게시글들을 조회하는 로직")
    public Response getRecentBoards(
            @PageableDefault(size = 16, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.findNewBoards(pageable));
    }

    // 분실물의 이름으로 게시글을 검색하는 로직 - 최신순
    @GetMapping("/boards/item/name")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 분실물 이름 기준(최신순 정렬)", notes = "분실물의 이름을 통해서 게시글을 검색하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemName", value = "게시글을 검색하기 위한 분실물의 이름", example = "test ItemName"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지의 번호, 기존값은 0으로 설정됨", example = "1")
    })
    public Response searchBoardByItem(@RequestParam("itemName") String itemName,
        @RequestParam(defaultValue = "0") int page,
        @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.searchBoardByItem(itemName, page, pageable));
    }

    // 분실물의 이름으로 게시글을 검색하는 로직 - 조회순
    @GetMapping("/boards/item/view")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 분실물 이름 기준(조회순 정렬)", notes = "분실물의 이름을 통해서 게시글을 검색하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemName", value = "게시글을 검색하기 위한 분실물의 이름", example = "test ItemName"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지의 번호, 기존값은 0으로 설정됨", example = "1")
    })
    public Response searchBoardByItem_viewCount(@RequestParam("itemName") String itemName,
        @RequestParam(defaultValue = "0") int page,
        @PageableDefault(size = 9, sort = {"viewCount", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.searchBoardByItem(itemName, page, pageable));
    }

    // 게시글 제목을 통한 게시글 조회 - 최신순
    @GetMapping("/boards/title/name")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 게시글 제목 기준(최신순 정렬)", notes = "게시글의 제목을 통해서 게시글을 검색하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "게시글을 검색하기 위한 게시글의 제목", example = "test Title"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지의 번호, 기존값은 0으로 설정됨", example = "1")
    })
    public Response searchBoardByTitle(@RequestParam("title") String title,
        @RequestParam(defaultValue = "0") int page,
        @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.searchBoardByTitle(title, page, pageable));
    }

    // 게시글 제목을 통한 게시글 조회 - 조회순
    @GetMapping("/boards/title/view")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 게시글 제목 기준(조회순 정렬)", notes = "게시글의 제목을 통해서 게시글을 검색하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "게시글을 검색하기 위한 게시글의 제목", example = "test Title"),
            @ApiImplicitParam(name = "page", value = "조회할 페이지의 번호, 기존값은 0으로 설정됨", example = "1")
    })
    public Response searchBoardByTitle_viewCount(@RequestParam("title") String title,
        @RequestParam(defaultValue = "0") int page,
        @PageableDefault(size = 9, sort = {"viewCount", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.searchBoardByTitle(title, page, pageable));
    }

    // 게시글 단일 조회
    @GetMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 단일 조회", notes = "id에 해당하는 게시글을 단건 조회하는 로직")
    @ApiImplicitParam(name = "id", value = "조회할 게시글의 id", example = "1")
    public Response getBoard(@PathVariable Long id) {
        return Response.success(boardService.findBoard(id));
    }

    // 일련 번호를 통하여 게시글 조회
    @GetMapping("/board/serial/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "시리얼 번호를 통한 게시글 조회", notes = "분실물을 저장할 때 사용하였던 시리얼 번호를 통하여 게시글 조회")
    @ApiImplicitParam(name = "serialNumber", value = "분실물 조회를 위한 일련번호", example = "123-456-789")
    public Response getBoardBySerialNumber(@PathVariable String serialNumber) {
        return Response.success(boardService.findBoardBySerialNumber(serialNumber));
    }

    // 게시글에 게시된 분실물이 자신의 물건인지 확인
    @GetMapping("/board/{id}/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "시리얼 번호를 통한 분실물 확인", notes = "시리얼 번호를 통하여 게시글에 게시된 품목이 나의 물건인지 확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "게시글의 번호", example = "1"),
            @ApiImplicitParam(name = "serialNumber", value = "확인할 시리얼 번호", example = "123-456-789")
    })
    public Response checkItem(@PathVariable Long id, @PathVariable String serialNumber) {
        return Response.success(boardService.checkItem(id, serialNumber));
    }

    //QR코드 생성
    @GetMapping("/board/QR/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "QR코드 생성", notes = "제품의 일련 번호를 통하여 QR코드를 생성하는 로직")
    @ApiImplicitParam(name = "serialNumber", value = "QR코드를 생성하기 위한 제품의 일련 번호", example = "123-456-789")
    public Response getQRCode(@PathVariable String serialNumber) {
        return Response.success(boardService.getQRCode(serialNumber));
    }

    // 게시글 생성
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "게시글 생성", notes = "분실물과 관련된 정보를 통하여 게시글을 생성하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mainCategory", value = "게시글 생성을 위한 카테고리의 id", example = "1"),
            @ApiImplicitParam(name = "subCategory", value = "게시글 생성을 위한 하위 카테고리의 id", example = "2"),
            @ApiImplicitParam(name = "leafCategory", value = "게시글 생성을 위한 최하단 카테고리의 id", example = "3")
    })
    public Response makeBoard(@ModelAttribute @Valid BoardCreateDto createDto,
                          @RequestParam("mainCategory") Long categoryId,
                          @RequestParam("subCategory") Long subCategoryId,
                          @RequestParam("leafCategory") Long leafCategoryId) {
        User user = getUser();
        return Response.success(boardService.makeBoard(createDto, user, categoryId, subCategoryId, leafCategoryId));
    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 수정", notes = "id값을 통하여 특정 게시글을 수정하는 로직")
    @ApiImplicitParam(name = "id", value = "수정할 게시글의 id", example = "1")
    public void editBoard(@ModelAttribute @Valid BoardEditRequestDto requestDto, @PathVariable Long id) {
        User user = getUser();
        boardService.editBoard(requestDto, user, id);
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 삭제", notes = "id값을 통하여 특정 게시글을 삭제하는 로직")
    @ApiImplicitParam(name = "id", value = "삭제할 게시글의 id", example = "1")
    public void deleteBoard(@PathVariable Long id) {
        User user = getUser();
        boardService.deleteBoard(user, id);
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
