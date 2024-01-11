package combus.backend.service;

import combus.backend.domain.Driver;
import combus.backend.domain.Reservation;
import combus.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;


    // 홈화면에서 유효한 예약 정보가 있을 경우 예약 정보 return
    public Reservation ReservationCheck(Long userId){

        //Optional<Reservation> reservation_check = reservationRepository.findByUserIdAndStatus(userId, "wait");
        Optional<Reservation> reservation_check = reservationRepository.findByUserId(userId);
        Reservation reservation;

        if (reservation_check.isPresent()) {
            reservation = reservation_check.get();
            System.out.println(reservation);
            return reservation;

        } else {
            return null;
        }
    }

    /*
    // 에약 내역 한번에 보기
    public List<Reservation> ReservationRecord(String userId){
        //데이터 베이스에서 회원 번호 조회해서 꺼내오기
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.isEmpty() ? null : reservations;
    }
     */
}