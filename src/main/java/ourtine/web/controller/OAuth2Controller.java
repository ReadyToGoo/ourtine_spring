package ourtine.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.request.SignupRequestDto;
import ourtine.web.dto.response.SignupResponseDto;
import ourtine.auth.service.OAuth2Service;
import ourtine.auth.utils.JwtUtil;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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


    @GetMapping("/api/auth/kakao/callback")
    @ResponseBody
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        String token = oAuth2Service.kakaoLogin(code);
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
        } catch(UnsupportedEncodingException e){
            log.error(e.getMessage());
        }
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>Query 문자열 테스트</title></head>");
        out.print("<body>");
        out.print("<h1>임시 회원 가입 완료 및 토큰 발급</h1>");
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

    @PostMapping("/api/user/signup")
    @ResponseBody
    public BaseResponseDto<SignupResponseDto> signupProgress(/*@AuthenticationPrincipal UserDetailsImpl userDetails, */@RequestBody SignupRequestDto signupRequestDto){
        log.info("/api/user/signup 접속");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        log.info("user = " + user);
        return oAuth2Service.signup(user, signupRequestDto);
    }
}
