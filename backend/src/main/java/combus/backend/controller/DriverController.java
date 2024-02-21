package combus.backend.controller;

import combus.backend.domain.Driver;
import combus.backend.dto.BusResponseDto;
import combus.backend.dto.LoginDriverResponseDto;
import combus.backend.repository.DriverRepository;
import combus.backend.request.LoginRequest;
import combus.backend.service.DriverService;
import combus.backend.util.ResponseCode;
import combus.backend.util.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private final DriverService driverService;

    @Autowired
    private DriverRepository driverRepository;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginDriverResponseDto>> login(@RequestBody LoginRequest LoginRequest,
                                                                      BindingResult bindingResult) {

        String loginId = LoginRequest.getLoginId();
        System.out.println("loginId: "+ loginId);

        Driver driver = driverService.authenticateDriver(loginId);

        // 회원 번호가 틀린 경우
        // if the authentication code is not valid
        if(driver == null){
            bindingResult.reject("login failed", "로그인 실패! 회원 번호를 확인해주세요.");
        }
        if(bindingResult.hasErrors()) {
            return ResponseData.toResponseEntity(ResponseCode.ACCOUNT_NOT_FOUND,null);
        }

        LoginDriverResponseDto loginDriver = new LoginDriverResponseDto(driver);
        return ResponseData.toResponseEntity(ResponseCode.SIGNIN_SUCCESS,loginDriver);
    }


}


