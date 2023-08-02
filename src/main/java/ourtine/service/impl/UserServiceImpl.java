package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.repository.UserRepository;
import ourtine.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void changeNickname(Long userId, String nickname) {
        System.out.println("닉네임:" + nickname);
        User user = userRepository.findById(userId).get();
        System.out.println("기존 닉네임:" + user.getNickname());
        user.updateNickname(nickname);
        System.out.println("업데이트된 유저 닉네임:" + user.getNickname());
        saveOrUpdateUser(user);
    }
}