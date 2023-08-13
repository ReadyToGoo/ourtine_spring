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
import ourtine.auth.info.impl.AppleOAuth2UserInfo;
import ourtine.auth.utils.JwtUtil;
import ourtine.domain.User;
import ourtine.domain.enums.AuthProvider;
import ourtine.domain.enums.UserRoleEnum;
import ourtine.domain.enums.UserStatus;
import ourtine.repository.UserRepository;

import java.net.URI;

@Slf4j(topic = "Apple Login")
@Service
public class AppleService{
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final String clientId;
    private final String redirectURI;

    public AppleService(UserRepository userRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
        clientId = "ab8915a9cb5583de48b6e54e901b247f";
        redirectURI = "http://localhost:8081/api/user/kakao/callback";
    }


    public String appleLoginPageRedirect(){
        return "redirect:";
    }

    // 애플 로그인
    public String appleLogin(String code) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청 ( 기존 CustomOAuth2Service 를 대체해서 RestTemplate 사용 )
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 애플 사용자 정보" 가져오기
        AppleOAuth2UserInfo appleUserInfo = getAppleUserInfo(accessToken);

        // 3. 필요시 회원가입
        User appleUser = registerAppleUserIfNeeded(appleUserInfo);

        // 4. JWT 토큰 반환
        String createdToken = jwtUtil.createAccessToken(appleUser.getId(), appleUser.getNickname(), appleUser.getUserStatus(), appleUser.getRole());

        return createdToken;
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가 코드 : " + code);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.apple.com")
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

    private AppleOAuth2UserInfo getAppleUserInfo(String accessToken) throws JsonProcessingException {
        log.info("Access Token : " + accessToken);

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.apple.com")
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

        Long id = jsonNode.get("id").asLong();

        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        String email = jsonNode.get("apple_account")
                .get("email").asText();

        log.info("애플 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new AppleOAuth2UserInfo(id);
    }


    // 필요 시 회원가입 처리
    private User registerAppleUserIfNeeded(AppleOAuth2UserInfo appleUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long appleId = appleUserInfo.getProviderId();

        User appleUser = userRepository.findByAuthProviderAndProviderId(AuthProvider.APPLE, appleId).orElse(null);

        // 회원가입이 되어있지 않다면 -> 임시 회원가입
        if (appleUser == null) {
            appleUser = User.builder()
                    .userRole(UserRoleEnum.USER)
                    .authProvider(AuthProvider.APPLE)
                    .providerId(appleUserInfo.getProviderId())
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
            appleUser.saveRefreshToken(jwtUtil.createRefreshToken(appleUser.getId(),appleUser.getRole()));

            userRepository.save(appleUser);
        }
        else if(appleUser.getUserStatus() == UserStatus.SIGNUP_PROGRESS){   // 임시회원가입 상태 시 로직 처리
            // 이건 컨트롤러 단에서 API 하나 더 만들어야할듯 ( Login 완료 API )
        } else {    // 회원가입 완료 상태 시 로직
            return appleUser;
        }
        return appleUser;
    }

}