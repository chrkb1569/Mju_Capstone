package mju.capstone.project.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentCreateDto {
    @NotBlank(message = "작성할 댓글의 내용을 입력해주세요")
    @ApiModelProperty(value = "댓글을 작성하는데에 필요한 내용", example = "Test Content")
    private String content;
}
