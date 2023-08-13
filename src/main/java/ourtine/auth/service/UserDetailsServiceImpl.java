package ourtine.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ourtine.auth.info.OAuth2UserInfo;
import ourtine.auth.info.OAuth2UserInfoFactory;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;
import ourtine.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic="UserDetailsServiceImpl : ")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // 언제, 어디서 호출? -> JwtAuthorizationFilter에서 호출 ( 인가 시 동작함
    // ( 여기서 JWT가 없을 때 => 필터를 그냥 통과하기에 -> service 단에서 임시회원가입 처리가 됐을 것이라 가정 )
    @Override
    public UserDetailsImpl loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("userDetails : userId = " +userId);
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        log.info("UserDetails : loadUserByUsername 호출");
        if(userOptional.isPresent()){
            User user = userOptional.get();
            OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(user.getProvider().toString(), user.getProviderId());
            return new UserDetailsImpl(user, oAuth2UserInfo);
        } else {
            throw new UsernameNotFoundException("해당 유저 아이디가 존재하지 않습니다.");
        }
    }
}
