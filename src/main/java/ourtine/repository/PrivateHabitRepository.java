package ourtine.repository;

import ourtine.domain.Habit;
import ourtine.domain.PrivateHabit;
import ourtine.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateHabitRepository extends JpaRepository<PrivateHabit, Long> {

}
