package ourtine.repository;

import ourtine.domain.Habit;
import ourtine.domain.PublicHabit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {

    // 날짜순 정렬 (PUBLIC + PRIVATE)
    @Query("select h from Habit h "+
            "where h.id in :followingHabitIds " +
            "and h.endDate >= CURDATE() " +
            "order by h.startDate desc")
    Slice<Habit> queryFindMyHabitsOrderByDate(List<Long> userId);

    // 시간순 정렬 (PUBLIC + PRIVATE)
    @Query("select h from Habit h "+
            "where h.id in :followingHabitIds " +
            "and h.endDate >= CURDATE() " +
            "order by h.startTime asc")
    Slice<Habit> queryFindMyHabitsOrderByTime(List<Long> userId);


}
