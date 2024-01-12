package combus.backend.dto;

import combus.backend.domain.User;
import lombok.*;

@Getter
@Setter
@Data
public class LoginUserResponseDto {
    private Long id;
    private String name;
    private String loginId;

    @Builder
    public LoginUserResponseDto(User user){
        name = user.getName();
        loginId = user.getLoginId();
        id = user.getId();
    }

}
