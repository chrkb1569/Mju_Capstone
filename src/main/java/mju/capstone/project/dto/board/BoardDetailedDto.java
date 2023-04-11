package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.dto.image.ImageDto;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardDetailedDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String itemName;
    private String serialNumber;

    private int viewCount;
    private List<ImageDto> images = new LinkedList<>();
    private LocalDateTime createdDate;

    public BoardDetailedDto toDto(Board board) {
        return BoardDetailedDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .itemName(board.getItemName())
                .serialNumber(board.getSerialNumber())
                .viewCount(board.getViewCount())
                .images(board.getImages().stream().map(image -> new ImageDto().toDto(image)).collect(Collectors.toList()))
                .createdDate(board.getCreatedDate())
                .build();
    }
}
