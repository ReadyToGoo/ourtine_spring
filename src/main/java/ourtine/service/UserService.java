package ourtine.service;

import ourtine.domain.User;

public interface UserService {
    public User findById(Long id);
    public void saveOrUpdateUser(User user);

    public void changeNickname(Long userId, String nickname);
    public void changeGoal(Long userId, String goal);
    public void changePushAlert(Long userId);
    public void changeMarketingPushAlert(Long userId);
}