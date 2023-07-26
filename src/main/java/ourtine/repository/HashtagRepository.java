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

    // 습관의 해시태그 찾기
    @Query("select hh.hashtag.name from HabitHashtag hh where hh.habit.id = :habitId")
    List<String> queryFindHabitHashtag(Long habitId);

    // 해시태그 있는지 여부
    boolean existsByName(String name);

    Optional<Hashtag> findHashtagByName(String name);

    // 해시태그 이름으로 해시태그 아이디 찾기
    @Query("select hh.hashtag.id from HabitHashtag hh where hh.hashtag.name like :keyword")
    Slice<Long> queryFindHashTagIdsByName(String keyword);

}
