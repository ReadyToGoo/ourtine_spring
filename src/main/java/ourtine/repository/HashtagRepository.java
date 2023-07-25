package ourtine.repository;

import ourtine.domain.Category;
import ourtine.domain.Hashtag;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {
    @Query("select hh.hashtag.name from HabitHashtag hh where hh.habit.id = :habitId")
    List<String> queryFindHabitHashtag(Long habitId);

}
