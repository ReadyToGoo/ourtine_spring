package ourtine.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ourtine.domain.User;
import ourtine.web.dto.response.UserSimpleProfileResponseDto;

public interface UserService {
    public User findById(Long id);
    public void saveOrUpdateUser(User user);

    public void changeNickname(Long userId, String nickname);
    public void changeGoal(Long userId, String goal);
    public void changePushAlert(Long userId);
    public void changeMarketingPushAlert(Long userId);
    public Slice<UserSimpleProfileResponseDto> searchByNickname(User user, String nickname, Pageable pageable);
}