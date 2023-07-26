package ourtine.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import ourtine.domain.mapping.HabitHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface HabitHashtagRepository extends JpaRepository<HabitHashtag,Long> {

    // 해시태그로 습관 아이디 찾기
    @Query("select hh.habit.id from HabitHashtag hh where hh.hashtag.id in :hashtagIds")
    Slice<Long> queryFindHabitIdsByHashtag(List<Long> hashtagIds);

}
