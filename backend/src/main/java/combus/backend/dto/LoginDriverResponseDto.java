package combus.backend.dto;

import combus.backend.domain.Driver;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginDriverResponseDto {

    // 버스 기사 정보
    private Long drvierId;
    private String driverName;
    private String cookie;

    public LoginDriverResponseDto(Driver driver, String sesseionId){
        drvierId = driver.getId();
        driverName = driver.getName();
        cookie = sesseionId;

    }

}
