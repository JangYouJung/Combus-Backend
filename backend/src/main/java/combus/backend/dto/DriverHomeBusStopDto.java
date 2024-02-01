package combus.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DriverHomeBusStopDto {
    private String arsId;        // 버스 정류장 고유 번호
    private String name;       // 버스 정류장 이름
    private double gpsX;       // 정류소 위치 x좌표 : 경도
    private double gpsY;       // 정류소 위치  y좌표 : 위도
    private int seq;           // 정류장 순서

    private int reserved_cnt;  // 승차 예약 인원수
    private int drop_cnt;      // 하차 예정 인원수

    private boolean wheelchair;// 휠체어 사용객 탑승 여부
}