package combus.backend.service;

import combus.backend.domain.BusStop;
import combus.backend.domain.Reservation;
import combus.backend.domain.User;
import combus.backend.dto.PassengerInfoDto;
import combus.backend.repository.BusStopRepository;
import combus.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final BusStopRepository busStopRepository;

    // 홈화면에서 유효한 예약 정보가 있을 경우 예약 정보 return
    public Reservation ReservationCheck(Long userId){

        Optional<Reservation>  valid_reservation = reservationRepository.findByUserIdAndDropStatus(userId, false);
        Reservation reservation;

        if (valid_reservation.isPresent()) {
            reservation = valid_reservation.get();
            System.out.println(reservation);
            return reservation;
        } else {
            return null;
        }
    }

    public Reservation findByReservationId(Long id) {
        Optional<Reservation> valid_reservation = reservationRepository.findById(id);

        Reservation reservation;
        if (valid_reservation.isPresent()) {
            reservation = valid_reservation.get();
            System.out.println(reservation);
            return reservation;
        } else {
            return null;
        }
    }

    public List<Reservation> findDetailOfBoardingStop(Long arsId){ // 승차 예정 (예약 중)
        List<Reservation> find_reservations = reservationRepository.findAllByBoardingStopArsIdAndBoardingStatusAndDropStatus(arsId, false, false);
        return find_reservations;
    }

    public List<Reservation> findDetailOfDropStop(Long arsId){ // 하차 예정 (현재 승차 중)
        List<Reservation> find_reservations = reservationRepository.findAllByDropStopArsIdAndBoardingStatusAndDropStatus(arsId, true, false);
        return find_reservations;
    }


    public List<PassengerInfoDto> findPassengers(Long arsId, Boolean boardingStatus) {

        List<Reservation> reservations;
        if(!boardingStatus){ // 승차 예정 승객 정보
            reservations = reservationRepository.findAllByBoardingStopArsIdAndBoardingStatusAndDropStatus(arsId, false, false);
        }
        else{ // 하차 예정 승객 정보
            reservations = reservationRepository.findAllByDropStopArsIdAndBoardingStatusAndDropStatus(arsId, true, false);
        }

        List<PassengerInfoDto> passengersList = new ArrayList<>();

        for(Reservation reservation : reservations){

            // [1] 승차 정류소 이름 저장하기
            BusStop findBoardingStop = reservation.getBoardingStop();
            String boardingStop = findBoardingStop.getName();

            // [2] 승객 장애 유형 가져오기
            String type;
            User passenger = reservation.getUser();
            if(passenger.isBlindness()){
                type = "시각 장애인";
            }
            else type = "휠체어";

            // [3] 하차 정류소
            BusStop findDropStop = reservation.getDropStop();
            String dropStop = findDropStop.getName();

            PassengerInfoDto passengerInfoDto = new PassengerInfoDto(type, boardingStop, dropStop);
            passengersList.add(passengerInfoDto);

        }
        return passengersList;

    }
}
