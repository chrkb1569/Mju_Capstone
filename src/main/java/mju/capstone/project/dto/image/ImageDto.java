package mju.capstone.project.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.image.Image;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImageDto {
    private String originalName;
    private String storedName;

    private String accessUrl;

    public ImageDto toDto(Image image) {
        return new ImageDto(image.getOriginName(), image.getStoredName(), image.getAccessUrl());
    }
}
