package combus.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndBusStopDto {
    private String arsId; // 버스 정류장 고유 번호
    private String name; // 버스 정류장 이름
    private String direction; // 진행 방향
    private double gpsX; // 정류소 위치 x좌표 : 경도
    private double gpsY; // 정류소 위치  y좌표 : 위도
    private int seq;

    public EndBusStopDto(String arsId, String name, String direction, double gpsX, double gpsY, int seq) {
        this.arsId = arsId;
        this.name = name;
        this.direction = direction;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.seq = seq;
    }
}
