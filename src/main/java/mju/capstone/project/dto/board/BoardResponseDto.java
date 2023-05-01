package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.board.Board;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String itemName;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public BoardResponseDto toDto(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .itemName(board.getItemName())
                .createdDate(board.getCreatedDate())
                .lastModifiedDate(board.getLastModifiedDate())
                .build();
    }
}
