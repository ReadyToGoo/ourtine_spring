package ourtine.repository;

import org.springframework.data.jpa.repository.Query;
import ourtine.domain.Habit;
import ourtine.domain.HabitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitSessionRepository extends JpaRepository<HabitSession,Long> {
    boolean existsByHabitId(Long aLong);

    // 습관 세션의 습관 아이디 조회
    @Query("select hs.habit.id from HabitSession hs where hs.id = :habitSessionId")
    Long queryFindHabitIdBySessionId(Long habitSessionId);

    // 습관 아이디로 오늘 진행되는 습관 세션 아이디조회
    @Query("select hs.id from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.date = CURDATE()")
    Long queryFindTodaySessionIdByHabitId(Long habitId);

    // 습관 아이디로 오늘 진행되는 습관 세션 아이디조회
    @Query("select hs from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.date = CURDATE()")
    Optional<HabitSession> queryFindTodaySessionByHabitId(Long habitId);

    // 습관 아이디로 삭제
    void deleteAllByHabit(Habit habit);
}
