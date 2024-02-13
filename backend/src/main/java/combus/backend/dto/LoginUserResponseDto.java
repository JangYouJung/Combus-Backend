package combus.backend.dto;

import combus.backend.domain.User;
import lombok.*;

@Getter
@Setter
@Data
public class LoginUserResponseDto {
    private Long id;
    private String name;
    //private String cookie;

    @Builder
    public LoginUserResponseDto(User user){
        name = user.getName();
        id = user.getId();
        //cookie= sessionId;
    }

}
