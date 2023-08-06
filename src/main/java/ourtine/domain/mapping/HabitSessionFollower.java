package ourtine.domain.mapping;

import lombok.AccessLevel;
import org.hibernate.annotations.ColumnDefault;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.HabitFollowerStatus;
import ourtine.domain.enums.Emotion;
import ourtine.domain.enums.Status;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitSessionFollower extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitSession_id",nullable = false)
    private HabitSession habitSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id",nullable = false)
    private User follower;

    private String videoUrl;

    // 해당 유저가 투표한 mvp 후보.
    private Long mvpVote;

    // 만족도
    private Long starRate;

    // 감정
    @Enumerated(value = EnumType.STRING)
    private Emotion emotion;

    @Enumerated(value = EnumType.STRING)
    private HabitFollowerStatus habitFollowerStatus = HabitFollowerStatus.ENTERED;

    @Enumerated(value = EnumType.STRING)
    private Status status=Status.ACTIVE;

    @Builder
    public HabitSessionFollower(HabitSession habitSession,User follower){
        this.habitSession = habitSession;
        this.follower = follower;
    }

    public void voteMvp(Long mvpVote){
        this.mvpVote  =mvpVote;
    }

    public void uploadVideo(String videoUrl){
        this.videoUrl = videoUrl;
        this.habitFollowerStatus = HabitFollowerStatus.COMPLETE;
    }

    //회고 작성
    public void writeReview(Long starRate, Emotion emotion){
        this.starRate = starRate;
        this.emotion = emotion;
    }

}