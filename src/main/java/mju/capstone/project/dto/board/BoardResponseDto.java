package mju.capstone.project.dto.board;

import lombok.*;
import mju.capstone.project.domain.board.Board;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String itemName;

    public BoardResponseDto toDto(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .itemName(board.getItemName())
                .build();
    }
}
