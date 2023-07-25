package ourtine.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ourtine.domain.enums.Day;

import java.util.List;

@Repository
public interface HabitDaysRepository {

    // 해당 요일에 진행되는 습관 ID
    @Query("select hd.habit.id from HabitDays hd " +
            "where hd.habit.id in :habitIds " +
            "and hd.day = :day")
    List<Long> queryFindMyHabitsByDay(List<Long> habitIds, Day day);
}
