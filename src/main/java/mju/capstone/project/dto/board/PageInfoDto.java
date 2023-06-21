package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.board.Board;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageInfoDto {
    private int totalPages;
    private int currentPages;
    private int pageSize;
    private boolean hasNext;

    public static PageInfoDto toDto(Page<Board> boards) {
        return new PageInfoDto(boards.getTotalPages(), boards.getNumber() + 1, boards.getNumberOfElements(), boards.hasNext());
    }
}
