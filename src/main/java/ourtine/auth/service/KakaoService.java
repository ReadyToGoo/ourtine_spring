package ourtine.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ourtine.auth.info.impl.KakaoOAuth2UserInfo;
import ourtine.auth.utils.JwtUtil;
import ourtine.domain.User;
import ourtine.domain.enums.Provider;
import ourtine.domain.enums.UserRoleEnum;
import ourtine.domain.enums.UserStatus;
import ourtine.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Slf4j(topic = "KAKAO Login")
@Service
public class KakaoService{

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    private final String clientId;

    private final String redirectURI;

    public KakaoService(UserRepository userRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        clientId = "ab8915a9cb5583de48b6e54e901b247f";
        redirectURI = "https://api.hyobn.shop/api/auth/kakao/callback";
    }

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;

    public void kakaoLoginPageRedirect(HttpServletResponse res) throws IOException {
        res.sendRedirect("https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectURI + "&response_type=code");
    }

    // 카카오 로그인
    public String kakaoLogin(String code) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String kakaoAccessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoOAuth2UserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);

        log.info("필요시 회원 가입");
        // 3. 필요시 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String appAccessToken = jwtUtil.createAccessToken(kakaoUser.getId(), kakaoUser.getNickname(), kakaoUser.getUserStatus(), kakaoUser.getRole());

        log.info("appAccessToken : " + appAccessToken);

        return appAccessToken;
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가 코드 : " + code);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);  // REST API Key
        body.add("redirect_uri", redirectURI);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoOAuth2UserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("Access Token : " + accessToken);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        Long kakaoId = jsonNode.get("id").asLong();

       /* String nickname = jsonNode.get("properties")
                .get("nickname").asText();*/

        log.info("카카오 사용자 정보: " + kakaoId);

        return new KakaoOAuth2UserInfo(kakaoId);
    }


    // 필요 시 회원가입 처리 ( => 이거 loadByUsername으로 하지 말고, OAuth Service 단에서 처리하는 거로 해야할듯 )
    private User registerKakaoUserIfNeeded(KakaoOAuth2UserInfo kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getProviderId();

        User kakaoUser = userRepository.findByProviderAndProviderId(Provider.KAKAO, kakaoId).orElse(null);

        // 회원가입이 되어있지 않다면 -> 임시 회원가입
        if (kakaoUser == null) {
            log.info("임시 회원가입 진행");
            kakaoUser = User.builder()
                    .userRole(UserRoleEnum.USER)
                    .provider(Provider.KAKAO)
                    .providerId(kakaoUserInfo.getProviderId())  // oauthId 설정
                    .userStatus(UserStatus.SIGNUP_PROGRESS) // 임시 회원가입
                    .habitCount(0)
                    .participationRate(0)
                    .imageUrl(null)
                    .goal(null)
                    .email(null)
                    .introduce(null)
                    .nickname(null)
                    .privacyAgreed(false)
                    .marketingAgreed(false)
                    .termsAgreed(false)
                    .build();
            log.info(""+kakaoUser.getProvider());
            log.info("ID = "+kakaoUser.getId());
            userRepository.save(kakaoUser);
            log.info("ID = "+kakaoUser.getId());
            kakaoUser.saveRefreshToken(jwtUtil.createRefreshToken(kakaoUser.getId(),kakaoUser.getRole()));
            userRepository.save(kakaoUser);
            log.info("kakaoUser.getRefreshToken() = " + kakaoUser.getRefreshToken());
        }
        return kakaoUser;
    }
}