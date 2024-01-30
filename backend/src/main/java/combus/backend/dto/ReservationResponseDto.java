package combus.backend.dto;

import combus.backend.domain.Reservation;
import combus.backend.repository.BusStopRepository;
import combus.backend.service.BusStopService;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
public class ReservationResponseDto {
    private LocalDate date;            //예약일
    private String busRouteName;    //버스 노선 번호
    private String boardingStop;    //승차 정류소
    private String dropStop;        //하차 정류소
    private String status;          // 승하차 상태

    public ReservationResponseDto(Reservation reservation){
        date = reservation.getCreatedAt();
        busRouteName = reservation.getBusRouteName();
        boardingStop = reservation.getBoardingStop().getName();
        dropStop = reservation.getDropStop().getName();

        // 승하차 상태 확인
        boolean boardingStatus = reservation.isBoardingStatus();
        boolean dropStatus = reservation.isDropStatus();

        if(boardingStatus == false) status = "승차 전: 버스 오는 중";
        else status = "탑승 완료: 목적지로 가는 중";

    }
}
