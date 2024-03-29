package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;
import ourtine.repository.UserRepository;
import ourtine.service.UserService;
import ourtine.util.CalculatorClass;
import ourtine.web.dto.response.UserSimpleProfileResponseDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CalculatorClass calculatorClass;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
    }
    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void changeNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ResponseMessage.WRONG_NICKNAME);
        }
        user.updateNickname(nickname);
        saveOrUpdateUser(user);
    }

    @Override
    public void changeGoal(Long userId, String goal) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        user.updateGoal(goal);
        saveOrUpdateUser(user);
    }


    @Override
    public void changeHabitCount(Long userId, Long habitCount) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        user.updateHabitCount(habitCount);
        saveOrUpdateUser(user);
    }

    @Override
    public void changeWeeklyLog(Long userId, String weeklyLog) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        user.updateUserWeeklyLog("["+formattedDate+"]"+weeklyLog);
        saveOrUpdateUser(user);
    }

    @Override
    public String getWeeklyLogPeriod(User user) {
        return calculatorClass.userWeeklyLogPeriod(user.getUserWeeklyLog());
    }
    @Override
    public String getWeeklyLogContents(User user) {
        return calculatorClass.userWeeklyLogContents(user.getUserWeeklyLog());
    }

    @Override
    public void changePushAlert(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        user.updatePushAlert();
        saveOrUpdateUser(user);
    }

    @Override
    public void changeMarketingPushAlert(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        user.updateMarketingPushAlert();
        saveOrUpdateUser(user);
    }

    @Override
    public Slice<UserSimpleProfileResponseDto> searchByNickname(User user, String nickname, Pageable pageable) {
        Slice<User> users = userRepository.querySearchByNickname(user.getId(), nickname, pageable);
        List<UserSimpleProfileResponseDto> userSimpleProfileResponseDtoList = users.getContent().stream()
                .map(m-> new UserSimpleProfileResponseDto(m))
                .collect(Collectors.toList());
        return new SliceImpl<>(userSimpleProfileResponseDtoList, users.getPageable(), users.hasNext());
    }
}