package ourtine.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Provider;
import ourtine.domain.enums.UserRole;
import ourtine.domain.enums.UserStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String introduce;

    private String email;
    private String imageUrl;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "habit")
    private List<Category> categories;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    private String goal;

    // 이용 약관 동의
    private boolean termsAgreed;

    // 개인 정보 수집 동의
    private boolean privacyAgreed;

    // 이벤트 정보 수신 동의
    private boolean marketingAgreed;

    private BigDecimal participationRate;

    private long habitCount;
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;
}
