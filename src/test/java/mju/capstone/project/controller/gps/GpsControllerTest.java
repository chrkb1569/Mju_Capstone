package mju.capstone.project.controller.gps;

import mju.capstone.project.dto.gps.GPSResponseDto;
import mju.capstone.project.service.gps.GpsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class GpsControllerTest {

    @Mock
    private GpsService gpsService;

    @InjectMocks
    private GpsController gpsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(gpsController)
                .build();
    }

    @Test
    @DisplayName(value = "GPS 실시간 정보 테스트")
    public void parseDatatest() throws Exception {
        //given
        GPSResponseDto responseDto = new GPSResponseDto("date", "longitude", "latitude");

        //stub
        given(gpsService.parseData()).willReturn(responseDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/gps"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(gpsService).parseData();
    }
}
