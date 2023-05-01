package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.board.Board;
import mju.capstone.project.dto.image.ImageDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardAndImageResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String itemName;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private double latitude;

    private double longitude;
    private List<ImageDto> images = new ArrayList<>();

    public BoardAndImageResponseDto toDto(Board board) {
        return BoardAndImageResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .itemName(board.getItemName())
                .createdDate(board.getCreatedDate())
                .lastModifiedDate(board.getLastModifiedDate())
                .latitude(board.getLatitude())
                .longitude(board.getLongitude())
                .images(board.getImages().stream()
                        .map(value -> new ImageDto().toDto(value))
                        .collect(Collectors.toList()))
                .build();
    }
}
