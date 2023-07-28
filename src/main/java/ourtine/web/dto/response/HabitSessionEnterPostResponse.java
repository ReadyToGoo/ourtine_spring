package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.HabitSession;
import ourtine.domain.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionEnterPostResponse {
    private Long sessionId;
    private Long followerId ;
    private Long habitId;

    public HabitSessionEnterPostResponse(HabitSession habitSession, User user){
        this.sessionId = habitSession.getId();
        this.followerId = user.getId();
        this.habitId = habitSession.getHabit().getId();
    }
}
