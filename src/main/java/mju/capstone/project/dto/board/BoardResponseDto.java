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
    private String title;
    private String writer;
    private String itemName;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private String serialNumber;

    private String category;
    private String subCategory;
    private String leafCategory;

    public BoardResponseDto toDto(Board board) {
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .writer(board.getWriter())
                .itemName(board.getItemName())
                .createdDate(board.getCreatedDate())
                .lastModifiedDate(board.getLastModifiedDate())
                .serialNumber(board.getSerialNumber())
                .category(board.getCategory().getCategoryName())
                .subCategory(board.getSubCategory().getCategoryName())
                .leafCategory(board.getLeafCategory().getCategoryName())
                .build();
    }
}
