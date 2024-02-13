//package combus.backend.controller;
//
//import combus.backend.service.GoogleVideoOcrService;
//import com.google.cloud.videointelligence.v1p3beta1.VideoAnnotationResults;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//public class GoogleVideoOcrController {
//    private final GoogleVideoOcrService googleVideoOcrService;
//
//    @Autowired
//    public GoogleVideoOcrController(GoogleVideoOcrService googleVideoOcrService) {
//        this.googleVideoOcrService = googleVideoOcrService;
//    }
//
//    @PostMapping("/video")
//    public ResponseEntity<String> detectTextFromVideo(@RequestParam("file") MultipartFile videoFile) {
//        try {
//            // 동영상에서 텍스트를 감지합니다.
//            VideoAnnotationResults results = googleVideoOcrService.detectTextFromVideo(videoFile);
//            if (results != null) {
//                // 텍스트 감지 결과를 클라이언트에 반환합니다.
//                return ResponseEntity.ok(results.toString());
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to detect text from video.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing video file.");
//        }
//    }
//}
