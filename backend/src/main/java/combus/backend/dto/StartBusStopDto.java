package combus.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StartBusStopDto {
    private String arsId; // 버스 정류장 고유 번호
    private String name; // 버스 정류장 이름
    private double gpsX; // 정류소 위치 x좌표 : 경도
    private double gpsY; // 정류소 위치  y좌표 : 위도

    public StartBusStopDto(String arsId, String name, double gpsX, double gpsY) {
        this.arsId = arsId;
        this.name = name;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }
}

