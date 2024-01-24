package combus.backend.service;

import combus.backend.domain.Reservation;
import combus.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;

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
        List<Reservation> find_reservations = reservationRepository.findAllByBoardingStopIdAndBoardingStatusAndDropStatus(arsId, false, false);
        return find_reservations;
    }

    public List<Reservation> findDetailOfDropStop(Long arsId){ // 하차 예정 (현재 승차 중)
        List<Reservation> find_reservations = reservationRepository.findAllByDropStopIdAndBoardingStatusAndDropStatus(arsId, true, false);
        return find_reservations;
    }

}
