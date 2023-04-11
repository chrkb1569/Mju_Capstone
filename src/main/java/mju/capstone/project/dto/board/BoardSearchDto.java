package mju.capstone.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardSearchDto {

    @NotBlank(message = "게시글을 검색하기 위한 값을 입력해주세요")
    private String searchValue;
}
