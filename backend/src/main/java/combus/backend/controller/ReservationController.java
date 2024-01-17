package combus.backend.controller;

import combus.backend.domain.Reservation;
import combus.backend.repository.ReservationRepository;
import combus.backend.request.ReservationRequest;
import combus.backend.service.ReservationService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createReservation(@RequestBody ReservationRequest reservationRequest,
                                                                                    @SessionAttribute(name = "userId", required = false)Long userId){

        System.out.println("로그인한 유저ID: " + userId);

        // 세션이 끊어진 경우 -> 로그인 화면으로 redirect
        if(userId == null){
            System.out.println("세션 expired");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/users/login"));
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }

        Long boardingStop = Long.parseLong(reservationRequest.getBoardingStop());   // 승차 정류소 번호 arsId
        Long dropStop = Long.parseLong(reservationRequest.getDropStop());           // 하파 정류소 번호 arsId
        Long vehId = Long.parseLong(reservationRequest.getVehId());                 // 버스 고유 ID (같은 노선의 버스라도 다 다른 고유값)
        String busRouteName = reservationRequest.getBusRouteNm();                  // 버스 노선명 (ex. 140번 버스)

        Reservation reservation = new Reservation(userId,vehId,boardingStop,dropStop,busRouteName);
        System.out.println(reservation.toString());
        reservationRepository.save(reservation);

        return ResponseData.toResponseEntity(ResponseCode.CREATE_RESERVATION_SUCCESS);
    }


}