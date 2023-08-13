package ourtine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ourtine.domain.enums.Provider;
import ourtine.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findByNickname(String nickname);
    Optional<User> findById(Long id);

    boolean existsByNickname(String nickname);

    @Query("select u from User u " +
            "where u.id not in (select b.blocked.id from Block b where b.blocker.id= :userId) " +
            "and u.id not in (select b.blocker.id from Block b where b.blocked.id = :userId) " +
            "and u.nickname like concat('%', :nickname, '%') " +
            "order by u.createdAt desc " )
    Slice<User> querySearchByNickname(Long userId, String nickname, Pageable pageable);

    Optional<User> findByProviderAndProviderId(Provider provider, Long providerId);



}
