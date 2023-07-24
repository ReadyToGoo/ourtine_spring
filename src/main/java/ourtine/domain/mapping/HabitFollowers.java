package ourtine.domain.mapping;

import ourtine.server.domain.Habit;
import ourtine.server.domain.User;
import ourtine.server.domain.common.BaseEntity;
import ourtine.server.domain.enums.Status;
import javax.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

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

    private boolean notification;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Builder
    public HabitFollowers(Habit habit, User follower, boolean notification ){
        this.habit = habit;
        this.follower = follower;
        this.notification = notification;
    }
}
