package ourtine.repository;

import ourtine.domain.mapping.HabitSessionFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitSessionFollowerRepository extends JpaRepository<HabitSessionFollower,Long> {

    // 팔로워의 습관 세션 입장 여부
    @Query("select count(hsf)>0 from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.id =: habitSessionId " +
            "and DATE_FORMAT(hsf.createdAt,'%Y-%m-%d') = hsf.habitSession.date ")
    boolean queryExistsByUserIdAndHabitSessionId (Long userId, Long habitSessionId);

    // 입장한 팔로워의 습관 세션 정보
    @Query("select hsf from HabitSessionFollower hsf " +
            "where hsf.follower.id = :userId " +
            "and hsf.habitSession.id = :habitSessionId " +
            "and DATE_FORMAT(hsf.createdAt,'%Y-%m-%d') = hsf.habitSession.date ")
    HabitSessionFollower queryGetTodayHabitSessionFollower (Long userId, Long habitSessionId);



}
