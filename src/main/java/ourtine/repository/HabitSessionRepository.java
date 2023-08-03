package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.HabitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitSessionRepository extends JpaRepository<HabitSession,Long> {

    // 습관 아이디로 오늘 진행되는 습관 세션 아이디조회
    @Query("select hs.id from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.date = CURDATE()" +
            "and hs.status = 'ACTIVE'")
    Long queryFindTodaySessionIdByHabitId(Long habitId);

    // 현재 진행중인 습관 아이디 조회
    @Query("select hs from HabitSession hs " +
            "where hs.date = CURDATE() "+
            "and hs.habit.endTime = :time "+
            "and hs.status = 'ACTIVE'")
    List<HabitSession> queryFindActiveSession(LocalTime time);


    // 습관 아이디로 오늘 진행되는 습관 세션 조회
    @Query("select hs from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.date = CURDATE()")
    Optional<HabitSession> queryFindTodaySessionByHabitId(Long habitId);

    // 참여율 - 습관 아이디로 종료된 습관 세션 조회
    @Query("select hs.id from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.status = 'INACTIVE'")
    List<Long> queryFindEndSessionIdsByHabitId (Long habitId);

    // 내 참여율 - 유저가 참여한 종료된 습관 세션 수 조회
    @Query("select count (hs) from HabitSession hs " +
            "where hs.habit in (select hf.habit from HabitFollowers hf where hf.follower = :user) " +
            "and hs.status = 'INACTIVE'")
    Long queryCountEndSessionsByUser (User user);

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabit(Habit habit);
}
