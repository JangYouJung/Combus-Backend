//package combus.backend.service;
//
//import com.google.api.gax.longrunning.OperationFuture;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.videointelligence.v1p3beta1.*;
//import com.google.protobuf.ByteString;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class GoogleVideoOcrService {
//
//    private final GoogleCredentials credentials;
//
//    public GoogleVideoOcrService() throws IOException {
//        // Google 서비스 계정의 인증 정보를 로드합니다.
//        this.credentials = GoogleCredentials.fromStream(
//                GoogleVideoOcrService.class.getClassLoader().getResourceAsStream("combusvideo-413101-ccb434c20a8f.json")
//        );
//    }
//
//    public VideoAnnotationResults detectTextFromVideo(MultipartFile videoFile) throws IOException {
//        try (VideoIntelligenceServiceClient client = VideoIntelligenceServiceClient.create()) {
//            // 동영상 파일 내용을 읽어옵니다.
//            ByteString fileContent = ByteString.copyFrom(videoFile.getBytes());
//
//            // 텍스트 감지 기능을 포함한 동영상 주석 요청을 생성합니다.
//            AnnotateVideoRequest request = AnnotateVideoRequest.newBuilder()
//                    .setInputContent(fileContent)
//                    .addFeatures(Feature.TEXT_DETECTION)
//                    .build();
//
//            // 동영상에서 텍스트를 감지합니다.
//            OperationFuture<AnnotateVideoResponse, AnnotateVideoProgress> future = client.annotateVideoAsync(request);
//            return future.get().getAnnotationResults(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
