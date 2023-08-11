package ourtine.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Provider;
import ourtine.domain.enums.UserRole;
import ourtine.domain.enums.UserStatus;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.UserCategory;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;
    private String imageUrl;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserCategory> userCategoryList;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("USER")
    private UserRole userRole;

    private String goal;

    @OneToMany(mappedBy = "follower")
    private List<HabitFollowers> habitFollowersList;

    // 이용 약관 동의
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean termsAgreed;


    // 개인 정보 수집 동의
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean privacyAgreed;

    // 이벤트 정보 수신 동의
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean marketingAgreed;

    @ColumnDefault("0")
    private Integer participationRate;

    @Column(nullable = false)
    @ColumnDefault("0")
    private long habitCount;

    // 푸쉬 알림 동의
    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean pushAlert;

    // 마케팅 푸쉬 알림 동의
    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean marketingPushAlert;


    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'NORMAL_ACTIVE'")
    private UserStatus userStatus;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateGoal(String goal) {
        this.goal=goal;
    }

    public void updateParticipationRate(Integer participationRate ){
        this.participationRate = participationRate;}

    public void updatePushAlert(){
        this.pushAlert = !pushAlert;
    }

    public void updateMarketingPushAlert(){
        this.marketingPushAlert = !marketingPushAlert;
    }

}