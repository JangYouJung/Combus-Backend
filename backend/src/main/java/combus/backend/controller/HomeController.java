package combus.backend.controller;

import combus.backend.domain.Reservation;
import combus.backend.domain.User;
import combus.backend.repository.ReservationRepository;
import combus.backend.service.ReservationService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class HomeController {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/home")
    public ResponseEntity<ResponseData<Reservation>> home(@SessionAttribute(name = "userId", required = false)Long userId) {

        // 세션이 끊어진 경우 -> 로그인 화면으로 redirect
        if(userId == null){
            System.out.println("세션 expired");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/users/login"));
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }

        System.out.println(userId);
        Reservation reserved = reservationService.ReservationCheck(userId);

        // 예약 내역이 없는 경우
        if(reserved == null){
            System.out.println("예약 내역이 없습니다.");
            return ResponseData.toResponseEntity(ResponseCode.EMPTY_RESERVATION_SUCCESS,null);
        }

        // 예약 내역이 존재하는 경우
        return ResponseData.toResponseEntity(ResponseCode.LOAD_RESERVATION_SUCCESS,reserved);
    }

}
