package mju.capstone.project.controller.board;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.dto.board.BoardCreateDto;
import mju.capstone.project.dto.board.BoardEditRequestDto;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.board.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 조회
    @GetMapping("/boards/all/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 전체 조회", notes = "특정 카테고리에 해당하는 모든 게시글들을 조회하는 로직")
    @ApiImplicitParam(name = "categoryId", value = "조회할 카테고리의 id", example = "1")
    public Response getBoards(@PathVariable Long categoryId) {
        return Response.success(boardService.findBoards(categoryId));
    }

    //게시글 전체 조회 - 최신글 16개만 조회
    @GetMapping("/boards/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "최신 게시글 조회", notes = "최근에 작성된 게시글들을 조회하는 로직")
    public Response getRecentBoards() {
        return Response.success(boardService.findNewBoards());
    }

    // 분실물의 이름으로 게시글을 검색하는 로직
    @GetMapping("/boards/item")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 분실물 이름 기준", notes = "분실물의 이름을 통해서 게시글을 검색하는 로직")
    public Response searchBoardByItem(@RequestParam("itemName") String itemName) {
        return Response.success(boardService.searchBoardByItem(itemName));
    }

    @GetMapping("/boards/title")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색 - 게시글 이름 기준", notes = "게시글의 이름을 통해서 게시글을 검색하는 로직")
    public Response searchBoardByTitle(@RequestParam("title") String title) {
        return Response.success(boardService.searchBoardByTitle(title));
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
    @ApiImplicitParam(name = "category", value = "게시글 생성을 위한 카테고리의 id", example = "1", type = "query")
    public void makeBoard(@ModelAttribute @Valid BoardCreateDto createDto,
                          @RequestParam("category") Long categoryId) {
        boardService.makeBoard(createDto, categoryId);
    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 수정", notes = "id값을 통하여 특정 게시글을 수정하는 로직")
    @ApiImplicitParam(name = "id", value = "수정할 게시글의 id", example = "1")
    public void editBoard(@ModelAttribute @Valid BoardEditRequestDto requestDto, @PathVariable Long id) {
        boardService.editBoard(requestDto, id);
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 삭제", notes = "id값을 통하여 특정 게시글을 삭제하는 로직")
    @ApiImplicitParam(name = "id", value = "삭제할 게시글의 id", example = "1")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}
