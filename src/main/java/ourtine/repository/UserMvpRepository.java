package ourtine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.mapping.UserMvp;

import java.util.List;

@Repository
public interface UserMvpRepository extends JpaRepository<UserMvp, Long> {
    List<UserMvp> findByHabitSessionId(Long sessionId);

    List<UserMvp> findByHabitSessionHabitAndUser_Id(Habit habit, Long user_id);
}
