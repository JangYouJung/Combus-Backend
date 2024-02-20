package combus.backend.controller;

import combus.backend.dto.BusStopReserveInfoDto;
import combus.backend.dto.EndBusStopDto;
import combus.backend.dto.VerifyBusNumberDto;
import combus.backend.service.GoogleVideoOcrService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/video")
public class GoogleVideoOcrController {

    private final GoogleVideoOcrService ocrService;

    @Autowired
    public GoogleVideoOcrController(GoogleVideoOcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping
    public ResponseEntity<ResponseData<VerifyBusNumberDto>> verifyBusNumberList(@RequestParam("videoFile") MultipartFile videoFile,
                                                                                   @RequestParam("busRouteNm") String busRouteNm) {

        List<VerifyBusNumberDto> verifyBusNumberList = new ArrayList<>();

        try {
            boolean result = ocrService.verifyBusNumber(videoFile, busRouteNm);
            VerifyBusNumberDto dto = new VerifyBusNumberDto(result);
            verifyBusNumberList.add(dto);
            return ResponseData.toResponseEntity(ResponseCode.BUS_VIDEO_CHECK_SUCCESS, dto);
        }
        catch (IOException e) {
            e.printStackTrace();
            // 서버 오류가 발생한 경우 빈 리스트를 반환합니다.
            return ResponseData.toResponseEntity(ResponseCode.BUS_VIDEO_CHECK_FAILED, null);
        }
    }
}
