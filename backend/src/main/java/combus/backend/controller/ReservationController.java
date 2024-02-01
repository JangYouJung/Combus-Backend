package combus.backend.controller;

import combus.backend.domain.BusStop;
import combus.backend.domain.Reservation;
import combus.backend.domain.User;
import combus.backend.repository.ReservationRepository;
import combus.backend.request.ReservationRequest;
import combus.backend.service.BusStopService;
import combus.backend.service.ReservationService;
import combus.backend.service.UserService;
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
    UserService userService;

    @Autowired
    BusStopService busStopService;

    @Autowired
    ReservationRepository reservationRepository;

    @PostMapping("/")   // 정류장 고유 번호로 예약하기
    public ResponseEntity<ResponseData> createReservation(
            @RequestBody ReservationRequest reservationRequest,
            @SessionAttribute(name = "userId", required = false)Long userId){

        System.out.println("로그인한 유저ID: " + userId);

        // 세션이 끊어진 경우 -> 로그인 화면으로 redirect
        if(userId == null){
            System.out.println("세션 expired");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/users/login"));
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }

        String boardingStopId = reservationRequest.getBoardingStop();   // 승차 정류소 번호 arsId
        String dropStopId = reservationRequest.getDropStop();           // 하파 정류소 번호 arsId
        Long vehId = Long.parseLong(reservationRequest.getVehId());     // 버스 고유 ID (같은 노선의 버스라도 다 다른 고유값)
        String busRouteName = reservationRequest.getBusRouteNm();       // 버스 노선명 (ex. 140번 버스)

        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + userId + " not found.");
            }

            BusStop boardingStop = busStopService.findByArsId(boardingStopId);
            if (boardingStop == null) {
                throw new IllegalArgumentException("Boarding stop with ARS ID " + boardingStopId + " not found.");
            }

            BusStop dropStop = busStopService.findByArsId(dropStopId);
            if (dropStop == null) {
                throw new IllegalArgumentException("Drop stop with ARS ID " + dropStopId + " not found.");
            }

            // 객체들이 유효한 경우
            Reservation reservation = new Reservation(user,vehId,boardingStop,dropStop,busRouteName);
            System.out.println(reservation.toString());
            reservationRepository.save(reservation);

            return ResponseData.toResponseEntity(ResponseCode.CREATE_RESERVATION_SUCCESS);

        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // 예외 정보를 콘솔에 출력
            return ResponseData.toResponseEntity(ResponseCode.FAILED_RESERVATION_CREATTION);
        }
    }

    @PostMapping("/stt")   // 정류장 이름으로 예약하기
    public ResponseEntity<ResponseData> createReservationByStName(
            @RequestBody ReservationRequest reservationRequest,
            @SessionAttribute(name = "userId", required = false)Long userId) {

        /************************************************
         *         시각 장애인이 음성으로 예약하는 경우          *
         *    버스 정류소 id가 아닌 정류소명으로 예약해야 하므로    *
         *        해당 기능을 위한 api를 따로 제작했다.         *
        *************************************************/

        System.out.println("로그인한 유저ID: " + userId);

        // 세션이 끊어진 경우 -> 로그인 화면으로 redirect
        if(userId == null){
            System.out.println("세션 expired");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/users/login"));
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }

        String boardingStopName = reservationRequest.getBoardingStop();         // 승차 정류소 이름
        String dropStopName = reservationRequest.getDropStop();                 // 하차 정류소 이름
        Long vehId = Long.parseLong(reservationRequest.getVehId());         // 버스 고유 ID (같은 노선의 버스라도 다 다른 고유값)
        String busRouteName = reservationRequest.getBusRouteNm();           // 버스 노선명 (ex. 140번 버스)

        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + userId + " not found.");
            }

            BusStop boardingStop = busStopService.findByName(boardingStopName);
            if (boardingStop == null) {
                throw new IllegalArgumentException("Boarding stop with Name " + boardingStopName + " not found.");
            }

            BusStop dropStop = busStopService.findByName(dropStopName);
            if (dropStop == null) {
                throw new IllegalArgumentException("Drop stop with Name" + dropStopName + " not found.");
            }

            // 객체들이 유효한 경우
            Reservation reservation = new Reservation(user,vehId,boardingStop,dropStop,busRouteName);
            System.out.println(reservation.toString());
            reservationRepository.save(reservation);

            return ResponseData.toResponseEntity(ResponseCode.CREATE_RESERVATION_SUCCESS);

        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // 예외 정보를 콘솔에 출력
            return ResponseData.toResponseEntity(ResponseCode.FAILED_RESERVATION_CREATTION);
        }




    }


}