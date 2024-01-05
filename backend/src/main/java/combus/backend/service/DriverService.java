package combus.backend.service;

import combus.backend.domain.Driver;
import combus.backend.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {

    @Autowired
    private final DriverRepository driverRepository;

    public Driver authenticateDriver(String loginId) {

        //데이터 베이스에서 회원 번호 조회해서 꺼내오기
        Optional<Driver> driver_check = driverRepository.findByLoginId(loginId);
        Driver driver;

        //회원 번호 존재시 해당 회원 리턴
        if (driver_check.isPresent()) {
            driver = driver_check.get();
            System.out.println(driver);

            return driver;

        } else {
            return null;
        }
    }
}
