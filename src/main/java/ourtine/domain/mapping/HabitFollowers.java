package ourtine.domain.mapping;

import lombok.Getter;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;
import javax.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class HabitFollowers extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @ManyToOne
    @JoinColumn(name = "follower_id",nullable = false)
    private User follower;


    @Enumerated(value = EnumType.STRING)
    private Status status =Status.ACTIVE ;

    @Builder
    public HabitFollowers(Habit habit, User follower ){
        this.habit = habit;
        this.follower = follower;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
