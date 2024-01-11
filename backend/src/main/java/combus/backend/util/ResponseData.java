package combus.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Getter
@Builder
@ToString
public class ResponseData<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String code;
    private final HttpStatus status;
    private final String detail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //ResponseEntity 없이 ResponseData만 필요할 때
    public static ResponseData of(ResponseCode responseCode) {
        return ResponseData.builder()
                .code(responseCode.name())
                .status(responseCode.getHttpStatus())
                .detail(responseCode.getDetail())
                .build();
    }

    //response data가 없을 때
    public static ResponseEntity<ResponseData> toResponseEntity(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseData.builder()
                        .code(responseCode.name())
                        .status(responseCode.getHttpStatus())
                        .detail(responseCode.getDetail())
                        .build()
                );
    }

    //response data가 있을 때
    public static <T> ResponseEntity<ResponseData<T>> toResponseEntity(ResponseCode responseCode,
                                                                       T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseData.<T>builder()
                        .code(responseCode.name())
                        .status(responseCode.getHttpStatus())
                        .detail(responseCode.getDetail())
                        .data(data)
                        .build()
                );
    }

    //response header가 있을 때
    public static <T> ResponseEntity<ResponseData<T>> toResponseEntity(ResponseCode responseCode,
                                                                       MultiValueMap<String, String> header, T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .header(String.valueOf(header))
                .body(ResponseData.<T>builder()
                        .code(responseCode.name())
                        .status(responseCode.getHttpStatus())
                        .detail(responseCode.getDetail())
                        .data(data)
                        .build()
                );
    }

}