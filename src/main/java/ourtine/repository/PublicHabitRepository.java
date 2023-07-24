package ourtine.repository;

import ourtine.domain.Habit;
import ourtine.domain.PrivateHabit;
import ourtine.domain.PublicHabit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicHabitRepository extends JpaRepository<PublicHabit, Long> {

    // 습관 날짜순 정렬
    @Query("select h from Habit h "+
            "where h.id in :followingHabitIds " +
            "and h.endDate >= CURDATE()" +
            "and h.habitStatus = 'PUBLIC' " +
            "order by h.startDate desc")
    Slice<PublicHabit> queryFindUserHabits(Long userId);


    // 검색 -습관 개설 순
    // 차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword)" +
            "order by h.createdAt desc")
    List<PublicHabit> querySearchHabit(Long hostId, String keyword);

     //검색 - 습관 시작일 순
     //차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword)" +
            "and h.startDate >= CURDATE()" +
            "order by h.createdAt asc")
    List<PublicHabit> querySearchHabitOrderByCreatedAt(Long hostId, String keyword);

    // 검색 - 모집중 ( 임박한 순으로 )
    // 차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword)" +
            "and h.followerLimit-h.followerCount>0" +
            "order by h.followerLimit-h.followerCount asc")
    List<PublicHabit> querySearchHabitOrderByStatus(Long hostId, String keyword);

    // 참여 - 카테고리
    // 차단 유저 필터링 & 모집중 필터
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and h.categoryId = :categoryId " +
            "and h.followerLimit-h.followerCount>0" +
            "order by h.followerLimit-h.followerCount asc")
    List<PublicHabit> querySearchHabitByCategory(Long hostId, Long categoryId);

    // 추천 습관
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and h.categoryId in (select uc.category.id from UserCategory uc where uc.user.id = :hostId and uc.status = 'ACTIVE') " +
            "and h.followerLimit-h.followerCount>0" +
            "order by h.followerLimit-h.followerCount asc")
    List<PublicHabit> recommendHabits(Long hostId);


}
