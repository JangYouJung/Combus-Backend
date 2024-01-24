package combus.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class DriverHomeResponseDto {
    private Long vehId;           // 버스 고유 ID
    private String busRouteName;    // 버스 노선 번호

    private int totalReserved;      // 총 승차 예약 인원수
    private int totalDrop;          // 총 하차 예정 인원수

    /* 따로 주는 것으로 변경
    private double gpsX;            // 버스 현재 위치 X 좌표
    private double gpsY;            // 버스 현재 위치 Y 좌표
    */

    private List<DriverHomeBusStopDto> BusStopList; // 각 정거장 정보
}