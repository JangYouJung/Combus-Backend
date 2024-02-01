package combus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PassengerInfoDto {
    private String type; // 장애 유형
    private String boardingStop;
    private String dropStop;


}
