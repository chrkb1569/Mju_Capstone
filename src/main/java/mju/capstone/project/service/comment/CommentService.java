package mju.capstone.project.service.comment;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.comment.Comment;
import mju.capstone.project.dto.comment.CommentCreateDto;
import mju.capstone.project.dto.comment.CommentEditDto;
import mju.capstone.project.dto.comment.CommentResponseDto;
import mju.capstone.project.exception.board.BoardNotFoundException;
import mju.capstone.project.exception.board.WriterNotMatchException;
import mju.capstone.project.exception.comment.CommentNotFoundException;
import mju.capstone.project.repository.board.BoardRepository;
import mju.capstone.project.repository.comment.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAll(Long boardId) {
        Board findItem = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        return commentRepository
                .findCommentsByBoard(findItem)
                .stream()
                .map(comment -> new CommentResponseDto().toDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional
    public void makeComment(CommentCreateDto createDto, Long boardId) {
        String writer = SecurityContextHolder.getContext().getAuthentication().getName();

        Board findItem = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        Comment comment = new Comment(writer, createDto.getContent(), findItem);

        findItem.getComments().add(comment);

        commentRepository.save(comment);
    }

    @Transactional
    public void editComment(CommentEditDto editDto, Long commentId) {
        Comment findItem = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if(!checkUserValidation(findItem)) throw new WriterNotMatchException();

        findItem.updateContent(editDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment findItem = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if(!checkUserValidation(findItem)) throw new WriterNotMatchException();

        commentRepository.deleteById(commentId);
    }

    public boolean checkUserValidation(Comment comment) {
        String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();

        String writer = comment.getWriter();

        return loginUser.equals(writer);
    }
}
