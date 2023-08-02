package ourtine.service;

import ourtine.domain.User;

public interface UserService {
    public void saveOrUpdateUser(User user);

    public void changeNickname(Long userId, String nickname);
}