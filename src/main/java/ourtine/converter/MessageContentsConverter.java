package ourtine.converter;

import org.springframework.stereotype.Component;
import ourtine.domain.Habit;
import ourtine.domain.User;

@Component
public class MessageContentsConverter {

    // 팔로우
    public String createContents(User sender) {
        return sender.getNickname() + "님이 팔로우하기 시작했습니다";
    }

    // 습관 초대
    public String createContents(User sender, Habit habit) {
        return sender.getNickname() + "님이 " + habit.getTitle() + "습관에 초대했습니다.";
    }

}
