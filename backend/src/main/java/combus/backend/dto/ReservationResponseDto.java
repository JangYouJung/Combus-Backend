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
    private boolean boardingStatus; //승차 상태
    private boolean dropStatus;     //하차 상태
    private boolean wheelChair;     //휠체어 탑승 여부

    public ReservationResponseDto(Reservation reservation){
        date = reservation.getCreatedAt();
        busRouteName = reservation.getBusRouteName();
        boardingStop = reservation.getBoardingStop().getName();
        dropStop = reservation.getDropStop().getName();
        boardingStatus = reservation.isBoardingStatus();
        dropStatus = reservation.isDropStatus();
        wheelChair = reservation.getUser().isWheelchair();
    }
}
