package combus.backend.controller;

import combus.backend.domain.Reservation;
import combus.backend.domain.User;
import combus.backend.dto.LoginUserResponseDto;
import combus.backend.repository.ReservationRepository;
import combus.backend.repository.UserRepository;
import combus.backend.request.LoginRequest;
import combus.backend.service.ReservationService;
import combus.backend.service.UserService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginUserResponseDto>> login(
            @RequestBody LoginRequest LoginRequest,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest
    ) {

        String loginId = LoginRequest.getLoginId();
        System.out.println("loginId: "+ loginId);

        User user = userService.authenticateUser(loginId);

        // 회원 번호가 틀린 경우
        if(user == null){
            bindingResult.reject("login failed", "로그인 실패! 회원 번호를 확인해주세요.");
        }
        if(bindingResult.hasErrors()) {
            return ResponseData.toResponseEntity(ResponseCode.ACCOUNT_NOT_FOUND,null);
        }

        // 로그인 성공시 세션 생성
        // 세션을 생성하기 전에 기존의 세션 파기

        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성

        LoginUserResponseDto loginUser = new LoginUserResponseDto(user, session.getId());

        // 세션에 user의 기본키 Id를 넣어줌
        session.setAttribute("userId", loginUser.getId());
        session.setMaxInactiveInterval(7200); // Session이 2시간동안 유지


        return ResponseData.toResponseEntity(ResponseCode.SIGNIN_SUCCESS,loginUser);

    }

}