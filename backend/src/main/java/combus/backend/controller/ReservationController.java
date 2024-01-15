package combus.backend.controller;

import combus.backend.repository.ReservationRepository;
import combus.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;


}
