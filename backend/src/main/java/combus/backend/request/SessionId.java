package combus.backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionId {
    private Long userId; // 현재 로그인한 사용자 기본키 아이디
}
