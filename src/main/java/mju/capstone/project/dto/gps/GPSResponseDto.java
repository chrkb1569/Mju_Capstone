package mju.capstone.project.dto.gps;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GPSResponseDto {
    private String currentDate;
    private String longitude;
    private String latitude;
}
