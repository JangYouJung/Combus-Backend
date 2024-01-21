package combus.backend.service;

import combus.backend.domain.Reservation;
import combus.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
