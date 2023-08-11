package ourtine.auth.repository;//package ourtine.auth.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import ourtine.auth.entity.User;
//import ourtine.domain.enums.AuthProvider;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<User,Long>{
//
//    // KakaoUser, AppleUser이 이미 회원가입 처리 되었는지를 알기 위한 메소드 -> KakaoService, AppleService에서 호출
//    Optional<User> findByAuthProviderAndProviderId(AuthProvider authProvider, Long providerId);
//    Optional<User> findById(Long id);
//
//}
