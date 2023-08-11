package ourtine.auth.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ourtine.domain.enums.UserRoleEnum;
import ourtine.domain.enums.UserStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;



// Token Provider ( JWT -> Token )

@Slf4j
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간, 토근 유지 시간 - 클라이언트와 협의 필요
    private static final Long accessTokenValidTime = Duration.ofMinutes(60).toMillis(); // 60분

    private static final Long refreshTokenValidTime = Duration.ofDays(14).toMillis(); // 14일

    @Value("${jwt.secret.key}")
    private String secretKey;

    // JWT Secret Key 관리
    private Key key;

    // JWT 암호화 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 토큰 substring ( BEARER_PREFIX 길이 = 7 만큼 )
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // 'Bearer ' <- 의 길이
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // JWT 토큰 생성, usernmae -> User Entity의 PK로 설정
    // Client 와 주고받을 JWT Token Spec : 유저아이디(PK), 닉네임, 유저 상태, 유저 권한(ROLE_USER, ROLE_ADMIN)
    private String createJwtForAccessToken(Long userId, String nickname, UserStatus userStatus, UserRoleEnum role, String type, Long tokenValidTime) {
        Claims claims = Jwts.claims();
        claims.put("username", userId.toString());  // User를 구분하기 위한 PK 값
        claims.put(AUTHORIZATION_KEY, role);
        claims.put("nickname", nickname);
        claims.put("userStatus", userStatus);

        return BEARER_PREFIX + Jwts.builder()
                .setHeaderParam("type", type)
                .setSubject("access")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    private String createJwtForRefreshToken(Long userId, UserRoleEnum role, String type, Long tokenValidTime) {
        Claims claims = Jwts.claims();
        claims.put("username", userId.toString());
        claims.put(AUTHORIZATION_KEY, role);

        return BEARER_PREFIX + Jwts.builder()
                .setHeaderParam("type", type)
                .setSubject("refresh")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }


    // JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");
            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }



    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            // 클라이언트에게 만료 여부 response body로 보내줌. ( status / is_expired )
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        log.info("req.getCookies :" + req.getCookies());
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }


    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // JWT에서 회원 정보 조회
    public String getUsername(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("username", String.class);
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public UserStatus getUserStatus(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userStatus", UserStatus.class);
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // 토큰 유효 및 만료 확인
    public boolean isExpired(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return false;
    }

    // refresh 토큰 존재 여부 확인
    public boolean existRefreshToken(String token){
        Header header = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getHeader();
        return header.get("type").toString().equals("refresh");
    }

    public boolean existAccessToken(String token){
        Header header = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getHeader();
        return header.get("type").toString().equals("access");
    }

    public String createAccessToken(Long userId, String nickname, UserStatus userStatus, UserRoleEnum role){
        return createJwtForAccessToken(userId, nickname, userStatus, role, "access", accessTokenValidTime);
    }

    public String createRefreshToken(Long userId, UserRoleEnum role){
        return createJwtForRefreshToken(userId, role,"refresh", refreshTokenValidTime);
    }
}