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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitSessionRepository extends JpaRepository<HabitSession,Long> {

    Optional<HabitSession> findByHabit_Id(Long habitId);

    // 현재 투표중인 습관 아이디 조회
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

    // 날짜로 습관 조회
    @Query("select hs.habit from HabitSession hs " +
            "where hs.date = :date")
    List<Habit> queryFindSessionsByDate(Date date);

    // 참여율 - 습관 아이디로 종료된 습관 세션 조회
    @Query("select hs.id from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.status = 'INACTIVE'" +
            "and hs.date >= :date")
    List<Long> queryFindEndSessionIdsByHabitId (Long habitId,Date date);

    // 참여율 - 종료된 세션 찾기
    @Query("select hs.id from HabitSession hs " +
            "where hs.habit.id = :habitId " +
            "and hs.status = 'INACTIVE'" )
    List<Long> queryFindEndSessionIdsByHabitId (Long habitId);

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabitId(Long habitId);
}
