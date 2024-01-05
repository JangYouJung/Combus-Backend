package combus.backend.controller;

import combus.backend.domain.User;
import combus.backend.repository.UserRepository;
import combus.backend.request.LoginRequest;
import combus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest LoginRequest) {

        String loginId = LoginRequest.getLoginId();
        System.out.println("loginId: "+ loginId);

        User authenticatedUser = userService.authenticateUser(loginId);

        if (authenticatedUser != null) {
            // Return user information or a token in the response
            return ResponseEntity.ok(authenticatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원 번호가 올바르지 않습니다.");
        }
    }
}