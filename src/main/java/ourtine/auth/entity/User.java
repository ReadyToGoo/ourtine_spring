package ourtine.auth.entity;//package ourtine.auth.entity;
//
//import javax.persistence.*;
//
//import lombok.*;
//import ourtine.domain.Category;
//import ourtine.domain.common.BaseEntity;
//import ourtine.domain.enums.AuthProvider;
//import ourtine.domain.enums.UserRoleEnum;
//import ourtine.domain.enums.UserStatus;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//
//@Getter
//@Entity
//@Builder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class User extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private Long id;
//    private String nickname;
//    private String introduce;
//    private String email;
//    private String imageUrl;
//    private String goal;
//    @OneToMany(mappedBy = "id", fetch=FetchType.EAGER)
//    private List<Category> categories;
//    // 이용 약관 동의
//    private Boolean termsAgreed;
//    // 개인 정보 수집 동의
//    private Boolean privacyAgreed;
//    // 이벤트 정보 수신 동의
//    private Boolean marketingAgreed;
//    private BigDecimal participationRate;
//    private long habitCount;
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum userRole;
//    @Enumerated(value = EnumType.STRING)
//    private UserStatus userStatus;
//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private AuthProvider authProvider;
//    @Column(nullable = false)
//    private Long providerId;
//    private String refreshToken;
//    public UserRoleEnum getRole() {
//        return userRole;
//    }
//    public void saveRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public void signup(String nickname, List<String> favoriteCategoryList, String introduce, String goal, Boolean termsAgreed, Boolean privacyAgreed, Boolean marketingAgreed){
//        this.nickname = nickname;
//        // this.categories =  favoriteCategoryList.stream().map(Category::new).collect(Collectors.toList());
//        // -> Category Mapping Table 에 저장하는 로직 필요 ( User : Category = N : M )
//        this.introduce =introduce;
//        this.goal = goal;
//        this.termsAgreed = termsAgreed;
//        this.privacyAgreed = privacyAgreed;
//        this.marketingAgreed = marketingAgreed;
//        this.userStatus = UserStatus.NORMAL_ACTIVE;
//    }
//
//
//}