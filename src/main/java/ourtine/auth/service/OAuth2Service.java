package ourtine.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ourtine.auth.dto.ApiResponse;
import ourtine.auth.dto.SignupRequestDto;
import ourtine.auth.dto.SignupResponseDto;
import ourtine.domain.User;
import ourtine.domain.enums.UserStatus;
import ourtine.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j(topic="OAuthService")
public class OAuth2Service {
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final UserRepository userRepository;

    public void kakaoLoginPageRedirect(HttpServletResponse res) throws IOException {
        kakaoService.kakaoLoginPageRedirect(res);
    }

    public String kakaoLogin(String code) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code);
    }

    public String appleLoginPageRedirect(){
        return appleService.appleLoginPageRedirect();
    }

    public String appleLogin(String code) throws JsonProcessingException {
        return appleService.appleLogin(code);
    }

//    @Transactional
    public ApiResponse<SignupResponseDto> signup(User user, SignupRequestDto signupRequestDto) {
        log.info("OAuth2Service SIGNUP - " + user.getId());
        // TO-DO : 임시회원가입상태 -> 회원가입 완료
        if (user == null) {
            return new ApiResponse<SignupResponseDto>(404,false, "해당 회원이 존재하지 않습니다.",new SignupResponseDto(user.getNickname()));
        } else if (user.getUserStatus() != UserStatus.SIGNUP_PROGRESS) {
            return new ApiResponse(400,false, "Bad Request : 이미 회원가입된 유저입니다.", new SignupResponseDto(user.getNickname()));
        } else {
            user.signup(signupRequestDto.getNickname(), signupRequestDto.getFavoriteCategoryList(), signupRequestDto.getIntroduce(), signupRequestDto.getGoal(), signupRequestDto.getTermsAgreed(), signupRequestDto.getPrivacyAgreed(), signupRequestDto.getMarketingAgreed());  // 유저 Entity 자체의 public method
            userRepository.save(user);  //  ! UserDetails 객체는 JPA에 의해 관리되는 Entity가 아니다.
        }
        return new ApiResponse<>(201, true, "Created", new SignupResponseDto(user.getNickname()));
    }
}

