package mju.capstone.project.service.gps;

import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.dto.gps.GPSResponseDto;
import mju.capstone.project.service.image.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GpsService {
    private final FileService fileService;

    @Transactional
    public GPSResponseDto parseData() {
        String gpsInfo = getGpsInfo();
        String[] parseData = gpsInfo.split("\n");
        String recentData = parseData[parseData.length - 1];
        String[] currentData = recentData.split(" ");

        return new GPSResponseDto(currentData[1], currentData[6].substring(0, currentData[6].length()-1), currentData[9]);
    }

    @Transactional
    public String getGpsInfo() {
        S3Object file = fileService.getFile("lastgps.txt");
        StringBuilder sb = new StringBuilder();

        try {
            byte[] bytes = file.getObjectContent().readAllBytes();

            for(byte value : bytes) {
                sb.append((char)value);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
