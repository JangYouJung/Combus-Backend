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

    private BusPosDto busPosDto;

    private List<DriverHomeBusStopDto> BusStopList; // 각 정거장 정보
}