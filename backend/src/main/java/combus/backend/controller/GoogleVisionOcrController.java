//2번 가장 많이 저장된 숫자 가져오기

package combus.backend.controller;

import combus.backend.service.GoogleVisionOcrService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import combus.backend.dto.VerifyBusNumberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GoogleVisionOcrController {

    private final GoogleVisionOcrService googleVisionOcrService;

    @Autowired
    public GoogleVisionOcrController(GoogleVisionOcrService googleVisionOcrService) {
        this.googleVisionOcrService = googleVisionOcrService;
    }

    @PostMapping("/image")
    public ResponseEntity<ResponseData<VerifyBusNumberDto>> verifyBusNumberList(@RequestParam("busRouteNm") String busRouteNm,
                                                                     @RequestParam("file") MultipartFile file) {

        List<VerifyBusNumberDto> verifyBusNumberList = new ArrayList<>();

        try {
            boolean result = googleVisionOcrService.isBusNumberMatching(busRouteNm, file);
            VerifyBusNumberDto dto = new VerifyBusNumberDto(result);
            verifyBusNumberList.add(dto);
            return ResponseData.toResponseEntity(ResponseCode.BUS_CHECK_SUCCESS, dto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseData.toResponseEntity(ResponseCode.BUS_CHECK_FAILED, null);
        }
    }
}

