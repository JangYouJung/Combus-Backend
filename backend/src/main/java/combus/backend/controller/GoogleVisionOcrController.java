//2번 가장 많이 저장된 숫자 가져오기

package combus.backend.controller;

import combus.backend.service.GoogleVisionOcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class GoogleVisionOcrController {

    private final GoogleVisionOcrService googleVisionOcrService;

    @Autowired
    public GoogleVisionOcrController(GoogleVisionOcrService googleVisionOcrService) {
        this.googleVisionOcrService = googleVisionOcrService;
    }

    @PostMapping("/image")
    public ResponseEntity<String> extractTextFromImage(@RequestParam("file") MultipartFile file) {
        try {
            // 이미지 파일로부터 숫자를 추출하여 가장 빈도가 높은 숫자 추출
            String mostFrequentNumber = googleVisionOcrService.extractTextAndNumbersFromImage(file);
            // 가장 빈도가 높은 숫자를 응답
            return ResponseEntity.ok(mostFrequentNumber);
        } catch (IOException e) {
            // 오류가 발생한 경우 500 상태 코드와 메시지를 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting text from image");
        }
    }
}
