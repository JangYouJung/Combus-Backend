package combus.backend.dto;

import combus.backend.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeResponseDto {
    private Long userId;
    private Long busId;
    private String status;
    private String createdAt;
    private Long boardingStop;
    private Long dropStop;


}
