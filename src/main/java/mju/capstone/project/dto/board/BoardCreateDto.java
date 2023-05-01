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
public class BoardCreateDto {

    @NotBlank(message = "게시글 작성을 위하여 제목을 입력해주세요")
    @ApiModelProperty(value = "게시글 작성을 위한 제목", example = "Test Title")
    private String title;

    @NotBlank(message = "게시글 작성을 위하여 내용을 입력해주세요")
    @ApiModelProperty(value = "게시글 작성을 위한 내용", example = "Test Content")
    private String content;

    @NotBlank(message = "게시글 작성을 위한 분실물 이름을 입력해주세요")
    @ApiModelProperty(value = "게시글 작성을 위한 분실물의 이름", example = "Test ItemName")
    private String itemName;

    @ApiModelProperty(value = "게시글 작성을 위한 분실물의 시리얼 번호(생략 가능)", example = "123-456-789")
    private String serialNumber;

    @ApiModelProperty(value = "분실물을 습득한 위치의 위도", example = "12.34")
    private Double latitude;

    @ApiModelProperty(value = "분실물을 습득한 위치의 경도", example = "34.56")
    private Double longitude;

    @ApiModelProperty(value = "게시글에 첨부할 사진")
    private List<MultipartFile> files = new ArrayList<>();
}
