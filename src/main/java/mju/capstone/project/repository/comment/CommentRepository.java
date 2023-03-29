package mju.capstone.project.repository.comment;

import mju.capstone.project.domain.board.Board;
import mju.capstone.project.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByBoard(Board board);
}
