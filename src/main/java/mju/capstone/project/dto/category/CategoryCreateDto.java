package mju.capstone.project.dto.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryCreateDto {

    @ApiModelProperty(value = "카테고리를 생성하는데에 필요한 카테고리의 이름 입력", example = "의류")
    @NotBlank(message = "생성할 카테고리의 이름을 입력해주세요")
    private String category;
}
