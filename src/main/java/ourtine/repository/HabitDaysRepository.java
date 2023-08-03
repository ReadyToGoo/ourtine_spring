package ourtine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.enums.Day;
import ourtine.domain.mapping.HabitDays;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface HabitDaysRepository extends JpaRepository<HabitDays,Long> {

    // 습관 아이디로 조회
    @Query("select hd.day from HabitDays hd " +
            "where hd.habit = :habit")
    List<Day> findDaysByHabit(Habit habit);

    // 해당 요일에 진행되는 습관 ID
    @Query("select hd.habit.id from HabitDays hd " +
            "where hd.habit.id in :habitIds " +
            "and hd.day = :day")
    Slice<Long> queryFindFollowingHabitsByDay(List<Long> habitIds, Day day, Pageable pageable);
    // 해당 요일에 진행되는 세션 id
    @Query("select hd.habit.id from HabitDays hd " +
            "where hd.habit.id in :habitIds " +
            "and hd.day = :day")

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabit(Habit habit);

    // 습관 시작 시간 & 요일로 습관 조회
    @Query("select hd.habit from HabitDays hd " +
            "where hd.habit.startTime = :startTime " +
            "and hd.habit.status = 'ACTIVE'" +
            "and hd.day = :dayName")
    List<Habit> queryFindHabitsByStartTime(LocalTime startTime, Day dayName);

}
