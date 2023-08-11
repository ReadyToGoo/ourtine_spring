package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ourtine.domain.Follow;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    // 내가 팔로잉 하는 유저
    Slice<Follow> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    // 나의 팔로워
    Slice<Follow> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    // 유저가 팔로잉 하는 유저
    @Query("select f from Follow f " +
            "where f.sender.id = :userId " +
            "and f.receiver.id not in (select b.blocked.id from Block b where b.blocker.id = :myId) " +
            "and f.receiver.id not in (select b.blocker.id from Block b where b.blocked.id = :myId)" +
            "order by f.createdAt desc " )
    Slice<Follow> queryFindBySenderIdOrderByCreatedAtDesc(Long userId, Long myId);

    // 유저의 팔로워
    @Query("select f from Follow f " +
            "where f.receiver.id = :userId " +
            "and f.sender.id not in (select b.blocked.id from Block b where b.blocker.id = :myId) " +
            "and f.sender.id not in (select b.blocker.id from Block b where b.blocked.id = :myId)"+
            "order by f.createdAt desc ")
    Slice<Follow> queryFindByReceiverIdOrderByCreatedAtDesc(Long userId, Long myId);
}
