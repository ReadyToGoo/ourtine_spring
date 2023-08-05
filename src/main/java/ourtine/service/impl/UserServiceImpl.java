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
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void changeNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).get();
        user.updateNickname(nickname);
        saveOrUpdateUser(user);
    }

    @Override
    public void changeGoal(Long userId, String goal) {
        User user = userRepository.findById(userId).get();
        user.updateGoal(goal);
        saveOrUpdateUser(user);
    }

}