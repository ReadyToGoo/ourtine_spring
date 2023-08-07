package ourtine.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
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

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabitId(Long habitId);

    // 습관의 해시태그  찾기
    @Query("select hh.hashtag.name from HabitHashtag hh where hh.habit.id = :habitId")
    List<String> queryFindHashtagNameByHabit(Long habitId);

    // 습관의 해시태그 찾기
    List<HabitHashtag> findByHabit(Habit habit);

    // 해시태그 이름으로 해시태그 아이디 찾기
    @Query("select hh.hashtag.id from HabitHashtag hh where hh.hashtag.name like :keyword")
    Slice<Long> queryFindHashTagIdsByName(String keyword);

    // 해시태그를 사용한 습관의 수
    long countByHashtag(Hashtag Hashtag);


}
