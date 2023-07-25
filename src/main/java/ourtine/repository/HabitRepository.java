package ourtine.repository;

import ourtine.domain.Habit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {

    // 아이디로 습관 정보 조회 (PUBLIC + PRIVATE)
    @Query("select h from Habit h " +
            "where h.id in :habitIds " +
            "and h.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and h.status = 'ACTIVE'" +
            "order by h.id desc ")
    Slice<Habit> queryFindHabitsById(List<Long> habitIds);

    // 습관 아이디로 습관 정보 조회 (PUBLIC ONLY)
    @Query("select h from Habit h " +
            "where h.id in :habitIds " +
            "and h.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and h.status = 'ACTIVE' " +
            "and h.habitStatus = 'PUBLIC'" +
            "order by h.id desc ")
    Slice<Habit> queryFindPublicHabitsById(List<Long> habitIds);

    // 습관 호스트 여부
    // TODO: 2023/07/25
   /* @Query("select if(h.host.id , 'YES', 'NO' ) as isHost from Habit h " +
            "where h.id = :habitId ")*/

    // 검색 - 습관 개설 순
    // 차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword) " +
            "and h.habitStatus = 'PUBLIC'" +
            "order by h.createdAt desc")
    Slice<Habit> queryFindHabitOrderByCreatedAt(Long hostId, String keyword);

    //검색 - 습관 시작일 순
    //차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword)" +
            "and h.startDate >= CURDATE()" +
            "and h.habitStatus = 'PUBLIC'" +
            "order by h.createdAt asc")
    Slice<Habit> queryFindHabitOrderByStartDate(Long hostId, String keyword);

    // 검색 - 모집중 ( 임박한 순으로 )
    // 차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and (h.title like :keyword " +
            "or (select hh.hashtag.name from HabitHashtag hh where hh.habit.id = h.id) like :keyword)" +
            "and h.followerLimit-h.followerCount>0" +
            "and h.habitStatus = 'PUBLIC'" +
            "order by h.followerLimit-h.followerCount asc")
    Slice<Habit> querySearchFindOrderByFollowerCount(Long hostId, String keyword);

    // 참여 - 카테고리
    // 차단 유저 필터링 & 모집중 필터
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and h.categoryId = :categoryId " +
            "and h.followerLimit-h.followerCount>0" +
            "and h.habitStatus = 'PUBLIC'" +
            "order by h.followerLimit-h.followerCount asc")
    List<Habit> querySearchHabitByCategory(Long hostId, Long categoryId);

    // 참여 - 추천 습관 목록
    // 차단 유저 필터링
    @Query("select h from Habit h " +
            "where h.host.id not in (select b.blocked.id from Block b where b.blocker.id = :hostId) " +
            "and h.host.id not in (select b.blocker.id from Block b where b.blocked.id = :hostId) " +
            "and h.categoryId in (select uc.category.id from UserCategory uc where uc.user.id = :userId and uc.status = 'ACTIVE') " +
            "and h.followerLimit-h.followerCount>0 " +
            "and h.habitStatus = 'PUBLIC'" +
            "and h.status = 'ACTIVE'" )
    Slice<Habit> queryGetRecommendHabits(Long userId);

}
