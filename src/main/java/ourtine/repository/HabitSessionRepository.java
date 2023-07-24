package ourtine.repository;

import org.springframework.data.jpa.repository.Query;
import ourtine.domain.HabitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitSessionRepository extends JpaRepository<HabitSession,Long> {
    boolean existsByHabitId(Long aLong);

    // 습관 세션의 습관 아이디 조회
    @Query("select hs.habit.id from HabitSession hs where hs.id = :habitSessionId")
    Long queryFindHabitIdBySessionId(Long habitSessionId);


}
