package ourtine.repository;

import ourtine.domain.mapping.HabitSessionFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitSessionFollowerRepository extends JpaRepository<HabitSessionFollower,Long> {

    // 팔로워의 습관 세션 입장 여부
    @Query("select count(hsf)>0 from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.id =: habitSessionId " )
    boolean queryExistsByUserIdAndHabitSessionId (Long userId, Long habitSessionId);

    // 입장한 팔로워의 습관 세션 정보
    @Query("select hsf from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.id = :habitSessionId " )
    HabitSessionFollower queryGetHabitSessionFollower (Long userId, Long habitSessionId);

    // 입장한 팔로워의 습관 세션 완료 여부
    @Query("select case " +
            "when count(hsf)>0 then hsf.completeStatus " + // 입장 기록 있으면 완료 여부 반환
            "else 'NOT_ENTERED' END " +                            // 없으면 완료 NOT_ENTERED
            "from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.habit.id =:habitId " +
            "and DATE_FORMAT(hsf.createdAt,'%Y-%m-%d') = hsf.habitSession.date ")
    boolean queryGetHabitSessionFollowerCompleteStatus (Long userId, Long habitId);

}
