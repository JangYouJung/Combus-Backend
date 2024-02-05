package combus.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequest {
    private Long userId;            // 현재 로그인한 사용자 정보

    private String boardingStop;
    private String dropStop;
    private String vehId;
    private String busRouteNm; //버스 번호
}
