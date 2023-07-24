package ourtine.domain.mapping;

import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

import javax.persistence.*;

public class HabitSessionFollower extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habitSession_id",nullable = false)
    private HabitSession habitSession;

    @OneToOne
    @JoinColumn(name = "follower_id",nullable = false)
    private User follower;

    // 해당 유저가 투표한 mvp 후보.
    private Long mvpVote;

    // 완료 여부를 Enum으로 만들어서 dtype 전략 사용할 필요 있는지 더 생각해보기.
    private boolean completeStatus;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
