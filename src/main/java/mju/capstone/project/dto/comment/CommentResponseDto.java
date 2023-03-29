package mju.capstone.project.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.comment.Comment;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private String content;

    private String writer;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getContent(), comment.getWriter(),
                comment.getCreatedDate(), comment.getLastModifiedDate());
    }
}
