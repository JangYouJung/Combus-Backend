package combus.backend.controller;

import combus.backend.domain.Reservation;
import combus.backend.dto.ReservationResponseDto;
import combus.backend.repository.ReservationRepository;
import combus.backend.service.ReservationService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserHomeController {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/home")
    public ResponseEntity<ResponseData<ReservationResponseDto>> home(@SessionAttribute(name = "userId", required = false)Long userId) {

        // 세션이 끊어진 경우 -> 로그인 화면으로 redirect
        if(userId == null){
            System.out.println("세션 expired");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/users/login"));
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }

        System.out.println(userId);
        Reservation reservation = reservationService.ReservationCheck(userId);

        // 예약 내역이 없는 경우
        if(reservation == null){
            System.out.println("예약 내역이 없습니다.");
            return ResponseData.toResponseEntity(ResponseCode.EMPTY_RESERVATION_SUCCESS,null);
        }

        // 예약 내역이 존재하는 경우
        ReservationResponseDto reservationDto = new ReservationResponseDto(reservation);
        return ResponseData.toResponseEntity(ResponseCode.LOAD_RESERVATION_SUCCESS,reservationDto);
    }

    @PutMapping("/home/{reservationId}")
    public ResponseEntity<ResponseData<Reservation>> changeReservationStatus(
            @PathVariable("reservationId") Long ReservationId
    )throws IOException {
        Reservation reservation = reservationService.findByReservationId(ReservationId);

        if(!reservation.isBoardingStatus()){ // 아직 탑승 전일 경우 -> boarding status를 true로 바꿔준다.
            reservationRepository.updateBoardingStatus(reservation.getId());
            return ResponseData.toResponseEntity(ResponseCode.RESERVATION_ONBOARDING_SUCCUESS,null);
        }
        else{ // 승차 완료일 경우 drop status를 변경해준다.
            reservationRepository.updateDropStatus(reservation.getId());
            return ResponseData.toResponseEntity(ResponseCode.RESERVATION_OFFBOARDING_SUCCESS,null);
        }
    }
}
