package combus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BusStopReserveInfoDto {

    /* 이전 화면에서 받아오는 정보이므로 생략
    private Long arsId;
    private String stopName;
    private String direction;
    private Long totalBoardingCnt;  // 총 예약자수
    private Long totalDropCnt;      // 총 하차 수
    */

    // 예약 정보
    private List<PassengerInfoDto> boardingInfo;
    private int boardingBlindCnt;
    private int boardingWheelchairCnt;

    // 하차 정보
    private List<PassengerInfoDto> dropInfo;
    private int dropBlindCnt;
    private int dropWheelchairCnt;

}
