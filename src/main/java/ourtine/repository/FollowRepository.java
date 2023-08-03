package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.Follow;
import ourtine.domain.User;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findBySenderAndReceiverId(User me, Long userId);

    // 내가 팔로잉 하는 유저
    Slice<Follow> findBySenderOrderByCreatedAtDesc(User me);

    // 나의 팔로워
    Slice<Follow> findByReceiverOrderByCreatedAtDesc(User me);

    // 유저가 팔로잉 하는 유저
    Slice<Follow> findBySenderIdOrderByCreatedAtDesc(Long userId/*, User me*/);

    // 유저의 팔로워
    Slice<Follow> findByReceiverIdOrderByCreatedAtDesc(Long userId/*, User me*/);
}
