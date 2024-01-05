package combus.backend.service;


import combus.backend.domain.User;
import combus.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public User authenticateUser(String loginId) {

        //데이터 베이스에서 회원 번호 조회해서 꺼내오기
        Optional<User> user_check = userRepository.findByLoginId(loginId);
        User user;

        //회원 번호 존재시 해당 회원 리턴
        if (user_check.isPresent()) {
            user = user_check.get();
            System.out.println(user);

            return user;

        } else {
            return null;
        }
    }
}

