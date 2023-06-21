package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardWithPagingResponseDto {
    private List<?> boards;
    private PageInfoDto pageInfoDto;

    public static BoardWithPagingResponseDto toDto(List<?> boards, PageInfoDto pageInfoDto) {
        return new BoardWithPagingResponseDto(boards, pageInfoDto);
    }
}
