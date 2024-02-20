package combus.backend.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.videointelligence.v1p3beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GoogleVideoOcrService {

    private final GoogleCredentials credentials;

    public GoogleVideoOcrService() throws IOException {
        // Google 서비스 계정의 인증 정보를 로드합니다.
        this.credentials = GoogleCredentials.fromStream(
                GoogleVideoOcrService.class.getClassLoader().getResourceAsStream("combusvideo-413101-ccb434c20a8f.json")
        );
    }

    public boolean verifyBusNumber(MultipartFile videoFile, String busRouteNm) throws IOException {
        try (VideoIntelligenceServiceClient client = VideoIntelligenceServiceClient.create()) {

            System.out.println("VideoOcrService 진입");
            // 동영상 파일 내용을 읽어옵니다.
            ByteString fileContent = ByteString.copyFrom(videoFile.getBytes());

            // 텍스트 감지 기능을 포함한 동영상 주석 요청을 생성합니다.
            AnnotateVideoRequest request = AnnotateVideoRequest.newBuilder()
                    .setInputContent(fileContent)
                    .addFeatures(Feature.TEXT_DETECTION)
                    .build();

            // 동영상에서 텍스트를 감지합니다.
            OperationFuture<AnnotateVideoResponse, AnnotateVideoProgress> future = client.annotateVideoAsync(request);
            AnnotateVideoResponse response = future.get();

            // 버스 번호가 영상에서 발견되었는지 확인합니다.
            for (VideoAnnotationResults result : response.getAnnotationResultsList()) {
                for (TextAnnotation textAnnotation : result.getTextAnnotationsList()) {
                    String text = textAnnotation.getText();

                    System.out.println("detected numner: "+ text);

                    // 텍스트가 숫자로만 구성되어 있고, 입력한 버스 번호와 일치하는지 확인합니다.
                    if (isNumeric(text) && text.equals(busRouteNm)) {
                        // 텍스트 세그먼트의 위치 정보를 가져옵니다.
                        TextSegment textSegment = textAnnotation.getSegments(0);
                        VideoSegment segment = textSegment.getSegment();
                        // 텍스트가 왼쪽 위에 있는지 확인합니다.
                        if (isInUpperLeft(segment)) {
                            System.out.println("BusNumber: "+ busRouteNm +" Detected");
                            return true; // 올바른 버스 번호가 발견되었습니다.
                        }
                    }
                }
            }
            return false; // 올바른 버스 번호가 발견되지 않았습니다.
        } catch (Exception e) {
            System.out.println("Error: "+ e);
            e.printStackTrace();
            return false;
        }
    }

    private boolean isNumeric(String text) {
        // 텍스트가 숫자로만 구성되어 있는지 확인합니다.
        return text.matches("\\d+");
    }

    private boolean isInUpperLeft(VideoSegment segment) {
        // 텍스트 세그먼트의 시작 시간이 0초이고, 좌표가 (0, 0)인 경우를 왼쪽 위로 간주합니다.
        return segment.getStartTimeOffset().getSeconds() == 0 && segment.getStartTimeOffset().getNanos() == 0;
    }
}

