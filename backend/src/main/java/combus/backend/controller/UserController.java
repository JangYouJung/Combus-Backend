package combus.backend.controller;

import combus.backend.domain.User;
import combus.backend.repository.UserRepository;
import combus.backend.request.LoginRequest;
import combus.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest LoginRequest, BindingResult bindingResult,
                                   HttpServletRequest httpServletRequest) {

        String loginId = LoginRequest.getLoginId();
        System.out.println("loginId: "+ loginId);

        User loginUser = userService.authenticateUser(loginId);

        // 회원 번호가 틀린 경우
        if(loginUser == null){
            bindingResult.reject("login failed", "로그인 실패! 회원 번호를 확인해주세요.");
        }
        if(bindingResult.hasErrors()) {
            return "redirect:/users/login";
        }


        // 로그인 성공 => 세션 생성
        // 세션을 생성하기 전에 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성

        // 세션에 user의 기본키 Id를 넣어줌
        session.setAttribute("userId", loginUser.getId());
        session.setMaxInactiveInterval(7200); // Session이 2시간동안 유지

        return "home";
    }

}