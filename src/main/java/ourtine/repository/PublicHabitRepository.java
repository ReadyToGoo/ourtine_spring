package ourtine.repository;

import ourtine.domain.Habit;
import ourtine.domain.PublicHabit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicHabitRepository extends JpaRepository<PublicHabit, Long> {

}
