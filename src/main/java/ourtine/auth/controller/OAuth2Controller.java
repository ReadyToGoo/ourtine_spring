package ourtine.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ourtine.auth.dto.ApiResponse;
import ourtine.auth.dto.SignupRequestDto;
import ourtine.auth.dto.SignupResponseDto;
import ourtine.auth.service.OAuth2Service;
import ourtine.auth.utils.JwtUtil;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "OAuth2Controller : ")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/api/auth/kakao/login")
    public void kakaoLoginPageRedirect(HttpServletResponse res) throws IOException {
        // TO-DO : Kakao 인증 페이지로 리다이렉트
        oAuth2Service.kakaoLoginPageRedirect(res);
    }

    @GetMapping("/api/auth/apple/login")
    public String appleLoginPageRedirect(){
        // TO-DO : Apple 인증 페이지로 리다이렉트
        return oAuth2Service.appleLoginPageRedirect();
    }

    /**
     * 카카오 로그인 진행 ( 인가코드를 통한 Access_Token, Access_Token을 통한 유저 정보 -> 우리 서비스의 토큰으로 만들어 보내줌
     * @param code : 사용자 카카오 로그인 시, 카카오 서버에서 발급해준 인증 코드
     * @param response : HttpServletResponse로 보내줄 데이터
     * @throws JsonProcessingException
     */
    @GetMapping("/api/auth/kakao/callback")
    @ResponseBody
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = oAuth2Service.kakaoLogin(code);
        // 시큐리티 필터 단에서 해주는 것? -> Filter로 이관해도 될듯 -> ㄴㄴ 컨트롤러에서 해주면 되는거였음.
        // JWTAuthenticationFilter implements UsernamepasswordAuthenticaitonFilter 를 사용하지 않아도 된다
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
        } catch(UnsupportedEncodingException e){
            log.error(e.getMessage());
        }
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/api/auth/apple/callback")
    @ResponseBody
    public void appleCallback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = oAuth2Service.appleLogin(code);
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
        } catch(UnsupportedEncodingException e){
            log.error(e.getMessage());
        }
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 회원가입 진행 ( 민서님과 논의가 필요한 부분 ) -> 회원가입 완료 후 모든 데이터 받아서 회원가입 처리하는 거로!
    @PostMapping("/api/user/signup")
    @ResponseBody
    public ApiResponse<SignupResponseDto> signupProgress(/*@AuthenticationPrincipal UserDetailsImpl userDetails, */@RequestBody SignupRequestDto signupRequestDto){
        log.info("/api/user/signup 접속");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        log.info("user = " + user);
        return oAuth2Service.signup(user, signupRequestDto);
    }
}
