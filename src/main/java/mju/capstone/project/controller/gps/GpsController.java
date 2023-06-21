package mju.capstone.project.controller.gps;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.gps.GpsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GpsController {

    private final GpsService gpsService;

    @GetMapping("/gps")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "GPS 데이터 수신", notes = "라즈베리파이가 생성한 파일로부터 GPS 데이터를 가져오는 로직")
    public Response parseData() {
        return Response.success(gpsService.parseData());
    }
}
