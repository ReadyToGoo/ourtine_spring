package ourtine.auth.dto;


// 임시 회원가입 -> 회원가입 완료 상태로 바꾸기 위해 필요한 Data 모음
// 회원가입 시 받아야할 데이터 종류????

import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequestDto {
    private String nickname;
    private List<String> favoriteCategoryList;
    private String introduce;
    private String goal;
    private Boolean termsAgreed;
    // 개인 정보 수집 동의
    private Boolean privacyAgreed;
    // 이벤트 정보 수신 동의
    private Boolean marketingAgreed;

}
