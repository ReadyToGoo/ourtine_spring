package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.enums.HabitFollowerStatus;
import ourtine.domain.mapping.HabitSessionFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitSessionFollowerRepository extends JpaRepository<HabitSessionFollower,Long> {

    // 팔로워의 습관 세션 입장 여부
    boolean existsByFollowerIdAndHabitSessionId (Long userId, Long habitSessionId);

    // 입장한 팔로워의 습관 세션 정보
    Optional<HabitSessionFollower> findByHabitSessionIdAndFollower (Long habitSessionId, User user);

    // 습관 세션의 팔로워 정보
    Slice<HabitSessionFollower> findByHabitSession_Id(Long habitSessionId);

    // 습관 세션의 투표 결과 조회
    @Query("select hsf.mvpVote from HabitSessionFollower hsf " +
            "where hsf.habitSession.id = :habitSessionId " )
    List<Long> queryGetHabitSessionVotes(Long habitSessionId);

    // 습관에 대한 유저의 회고
    Slice<HabitSessionFollower> findByFollowerIdAndHabitSessionHabitId (Long userId, Long habitId);

    // 입장한 팔로워의 습관 세션 완료 여부
    @Query("select case " +
            "when count(hsf)>0 then hsf.habitFollowerStatus " + // 입장 기록 있으면 완료 여부 반환
            "else 'NOT_ENTERED' END " +                            // 없으면 완료 NOT_ENTERED
            "from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.habit.id =:habitId " +
            "and hsf.createdAt = hsf.habitSession.date ")
    HabitFollowerStatus queryGetHabitSessionFollowerCompleteStatus (Long userId, Long habitId);

    // 유저가 참여한 습관 세션의 횟수
    @Query("select count (hsf) from HabitSessionFollower hsf " +
            "where hsf.follower = :user " +
            "and hsf.habitSession.habit = :habitId " +
            "and hsf.habitSession.id in :sessionIds" )
    Long queryGetParticipateSessionNumber (User user, Long habitId, List<Long> sessionIds);

    // 유저가 참여한 습관 세션의 만족도
    @Query("select hsf.starRate from HabitSessionFollower hsf " +
            "where hsf.follower = :user " +
            "and hsf.habitSession.habit = :habitId " +
            "and hsf.habitSession.id in :sessionIds" )
    List<Long> queryGetStarRate (User user, Long habitId, List<Long> sessionIds);

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabitSession_Habit(Habit habit);
}
