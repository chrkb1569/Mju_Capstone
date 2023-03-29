package mju.capstone.project.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardEditRequestDto {

    @NotBlank(message = "수정할 제목을 입력해주세요")
    @ApiModelProperty(value = "게시글 수정을 위한 제목", example = "Test Title")
    private String title;

    @NotBlank(message = "수정할 내용을 입력해주세요")
    @ApiModelProperty(value = "게시글 수정을 위한 내용", example = "Test Content")
    private String content;

    @ApiModelProperty(value = "게시글을 수정할 경우, 추가할 이미지")
    private List<MultipartFile> addImage = new ArrayList<>(); // 기존 이미지에 추가할 이미지

    @ApiModelProperty(value = "게시글을 수정할 경우, 삭제할 이미지")
    private List<Integer> deleteImage = new ArrayList<>(); // 기존 이미지에서 삭제할 이미지의 번호
}
