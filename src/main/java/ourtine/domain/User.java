package ourtine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Provider;
import ourtine.domain.enums.UserRoleEnum;
import ourtine.domain.enums.UserStatus;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.UserCategory;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nickname;
    private String introduce;
    private String imageUrl;
    private String goal;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Provider provider;
    @Column(nullable = false)
    private Long providerId;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserCategory> userCategoryList;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("USER")
    private UserRoleEnum userRole;

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

//    @Column
//    @ColumnDefault("아직 작성한 위클리로그가 없습니다.")
//    private String weeklyLog;


    // 푸쉬 알림 동의
    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean pushAlert;

    // 마케팅 푸쉬 알림 동의
    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean marketingPushAlert;

    private String refreshToken;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
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
//    public void updateWeeklyLog(String weeklyLog) {
//        this.weeklyLog=weeklyLog;
//    }


    public void updateHabitCount(Long habitCount) {
        this.habitCount=habitCount;
    }
    public void updateParticipationRate(Integer participationRate ){
        this.participationRate = participationRate;}
    public void updatePushAlert(){
        this.pushAlert = !pushAlert;
    }
    public void updateMarketingPushAlert(){
        this.marketingPushAlert = !marketingPushAlert;
    }

    public UserRoleEnum getRole() {
        return userRole;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void signup(String nickname, String introduce, String goal, Boolean termsAgreed, Boolean privacyAgreed, Boolean marketingAgreed){
        this.nickname = nickname;
        this.introduce =introduce;
        this.goal = goal;
        this.termsAgreed = termsAgreed;
        this.privacyAgreed = privacyAgreed;
        this.marketingAgreed = marketingAgreed;
        this.userStatus = UserStatus.NORMAL_ACTIVE;
    }

}