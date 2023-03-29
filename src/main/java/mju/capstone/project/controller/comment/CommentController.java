package mju.capstone.project.controller.comment;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.dto.comment.CommentCreateDto;
import mju.capstone.project.dto.comment.CommentEditDto;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 전체 조회
    @GetMapping("/comments/all/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "댓글 전체 조회", notes = "boardId를 통하여 게시글을 찾은 뒤, 해당 게시글에 작성된 댓글을 전체 조회하는 로직")
    @ApiImplicitParam(name = "boardId", value = "게시글에 작성된 모든 댓글을 확인하기 위한 boardId", example = "1")
    public Response findAllComments(@PathVariable Long boardId) {
        return Response.success(commentService.findAll(boardId));
    }

    // 댓글 생성
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "댓글 생성", notes = "boarId를 통하여 게시글을 찾은 뒤, 해당 게시글에 댓글을 작성하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "createDto", value = "댓글을 작성하는데에 필요한 정보를 입력"),
            @ApiImplicitParam(name = "board", value = "게시글을 찾기 위한 boardId", example = "1", dataType = "query")
    })
    public void makeComment(@RequestParam("board") Long boardId, @RequestBody @Valid CommentCreateDto createDto) {
        commentService.makeComment(createDto, boardId);
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "댓글 수정", notes = "id를 통하여 게시글을 찾은 뒤, 해당 게시글에 작성한 댓글을 수정하는 로직")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "editDto", value = "댓글을 수정하는데에 필요한 정보를 입력"),
            @ApiImplicitParam(name = "id", value = "댓글이 작성되어 있는 게시글의 id", example = "1")
    })
    public void editComment(@RequestBody @Valid CommentEditDto editDto, @PathVariable Long id) {
        commentService.editComment(editDto, id);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "댓글 삭제", notes = "id에 해당하는 댓글을 삭제하는 로직")
    @ApiImplicitParam(name = "id", value = "삭제할 댓글의 id", example = "1")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
