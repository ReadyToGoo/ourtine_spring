package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.domain.mapping.HabitSessionFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitSessionFollowerRepository extends JpaRepository<HabitSessionFollower,Long> {


    // 팔로워의 습관 세션 입장 여부
    boolean existsByFollowerIdAndHabitSessionHabitId (Long follower_id, Long habitSession_habit_id);

    // 팔로워의 습관 세션 입장 여부
    boolean existsByFollowerIdAndHabitSessionId (Long follower_id, Long habitSession_id);

    @Query("select hsf from HabitSessionFollower hsf " +
            "where hsf.habitSession.id = :seesionId " +
            "and hsf.follower.id = :userId " +
            "and hsf.habitFollowerStatus = 'COMPLETE'")
    Optional<HabitSessionFollower> queryFindCompleteUser(Long sessionId, Long userId);

    // 입장한 팔로워의 습관 세션 정보
    Optional<HabitSessionFollower> findByHabitSession_IdAndFollowerId (Long habitSessionId, Long user);

    // 습관 세션의 투표 결과 조회
    @Query("select hsf.mvpVote from HabitSessionFollower hsf " +
            "where hsf.habitSession.id = :habitSessionId " )
    List<Long> queryGetHabitSessionVotes(Long habitSessionId);

    // 습관에 대한 유저의 회고
    Slice<HabitSessionFollower> findByFollowerIdAndHabitSessionHabitId (Long userId, Long habitId);


    // 유저가 참여한 습관 세션의 횟수
    @Query("select count (hsf) from HabitSessionFollower hsf " +
            "where hsf.follower = :user " +
            "and hsf.habitSession.habit.id = :habitId " +
            "and hsf.habitSession.id in :sessionIds" )
    Long queryGetParticipateSessionNumber (User user, Long habitId, List<Long> sessionIds);

    // 유저가 참여한 습관 세션의 만족도
    @Query("select hsf.starRate from HabitSessionFollower hsf " +
            "where hsf.starRate != null " +
            "and hsf.habitSession.habit.id = :habitId " )
    List<Integer> queryGetStarRate (Long habitId);

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabitSession_Habit_Id(Long habitId);


    // 이번 주의 세션 인증 영상
    @Query("select hsf from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitFollowerStatus = 'COMPLETE' " +
            "and hsf.createdAt >= :startDate")
    List<HabitSessionFollower> getFollowerSessionInfo(LocalDateTime startDate, Long userId);
}
