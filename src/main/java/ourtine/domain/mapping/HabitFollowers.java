package ourtine.domain.mapping;

import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

import javax.persistence.*;

public class HabitFollowers extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @OneToOne
    @JoinColumn(name = "follower_id",nullable = false)
    private User follower;

    private boolean notification;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
