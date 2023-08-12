package ourtine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ourtine.domain.mapping.UserMvp;

import java.util.List;

@Repository
public interface UserMvpRepository extends JpaRepository<UserMvp, Long> {
    List<UserMvp> findByHabitSessionId(Long sessionId);

    @Query
    ("select um from UserMvp um where um.habitSession.habit.id = :habitId and um.user.id = :userId")
    List<UserMvp> queryFindByHabitIdAndUserId(Long habitId, Long userId);
}
