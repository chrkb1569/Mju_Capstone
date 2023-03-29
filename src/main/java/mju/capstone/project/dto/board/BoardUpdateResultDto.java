package mju.capstone.project.dto.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardUpdateResultDto {

    private List<MultipartFile> fileList = new ArrayList<>();

    private List<Image> addedImages = new ArrayList<>();

    private List<Image> deletedImages = new ArrayList<>();
}
