package ourtine.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.converter.CategoryListsConverter;
import ourtine.domain.Category;
import ourtine.domain.enums.CategoryList;
import ourtine.repository.CategoryRepository;
import ourtine.service.UserCategoryService;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.request.SignupRequestDto;
import ourtine.web.dto.response.SignupResponseDto;
import ourtine.domain.User;
import ourtine.domain.enums.UserStatus;
import ourtine.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic="OAuthService")
public class OAuth2Service {
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final UserCategoryService userCategoryService;
    private final UserRepository userRepository;
    private final CategoryRepository categorRepository;

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

    @Transactional
    public BaseResponseDto<SignupResponseDto> signup(User user, SignupRequestDto signupRequestDto) {
        log.info("OAuth2Service SIGNUP - " + user.getId());
        // TO-DO : 임시회원가입상태 -> 회원가입 완료
        if (user == null) {
            return new BaseResponseDto<>(404, false, "해당 회원이 존재하지 않습니다.",new SignupResponseDto(user.getNickname()));
        } else if (user.getUserStatus() != UserStatus.SIGNUP_PROGRESS) {
            return new BaseResponseDto<>(400, false, "Bad Request : 이미 회원가입된 유저입니다.", new SignupResponseDto(user.getNickname()));
        } else {
            user.signup(signupRequestDto.getNickname(), signupRequestDto.getIntroduce(), signupRequestDto.getGoal(), signupRequestDto.getTermsAgreed(), signupRequestDto.getPrivacyAgreed(), signupRequestDto.getMarketingAgreed());  // 유저 Entity 자체의 public method
            userRepository.saveAndFlush(user);
            List<CategoryList> categoryListList = new CategoryListsConverter().convert(signupRequestDto.getFavoriteCategoryList());
            assert categoryListList != null;
            List<Category> categoryList;
            try {
                categoryList = categoryListList.stream().map(v -> categorRepository.findByName(v).orElseThrow( () -> new IllegalArgumentException("유효하지 않은 카테고리명 입력입니다."))).collect(Collectors.toList());
            } catch (IllegalArgumentException e){
                return new BaseResponseDto<>(400, true, "Bad Request : " + e.getMessage(), new SignupResponseDto(user.getNickname()));
            }
            User savedUser = userRepository.findById(user.getId()).orElse(null);
            userCategoryService.saveCategories(savedUser, categoryList);
        }
        return new BaseResponseDto<>(201, true, "Created", new SignupResponseDto(user.getNickname()));
    }
}

