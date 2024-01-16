package combus.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusResponseDto {
    private String vehId; // 버스 고유 ID
    private String busRouteId; // 버스 노선 ID
    private String busRouteAbrv; // 버스 노선명
    private int low; // 저상 버스 여부

    public BusResponseDto(String vehId, String busRouteId, String busRouteAbrv, int low) {
        this.vehId = vehId;
        this.busRouteId = busRouteId;
        this.busRouteAbrv = busRouteAbrv;
        this.low = low;
    }
}
