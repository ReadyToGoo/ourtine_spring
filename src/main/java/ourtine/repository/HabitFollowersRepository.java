package ourtine.repository;

import org.springframework.stereotype.Repository;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.mapping.HabitFollowers;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface HabitFollowersRepository extends JpaRepository<HabitFollowers,Long> {

    // 습관 참여 여부
    @Query("select count(hf)>0 from HabitFollowers hf " +
            "where hf.habit.id = :habitId " +
            "and hf.habit.host.id = :userId")
    boolean queryExistsByUserIdAndHabitId (Long habitId, Long userId);

    // 습관 팔로워 정보 조회
    @Query("select hf.follower from HabitFollowers hf where hf.habit.id = :habitId and hf.status='ACTIVE' and hf.habit.status = 'ACTIVE'")
    Slice<User> queryFindHabitFollowers(Long habitId);

    // 참여하고 있는 습관 아이디 조회
    @Query("select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId and hf.status='ACTIVE' and hf.habit.status = 'ACTIVE'")
    Slice<Long> queryFindMyFollowingHabitIds(Long userId);

    // 참여하고 있는 습관 정보 조회
    @Query("select hf.habit from HabitFollowers hf where hf.follower.id = :userId and hf.status='ACTIVE'")
    Slice<Habit> queryFindMyFollowingHabits(Long userId);

    // 참여하고 있는 습관 아이디 조회
    @Query("select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId and hf.status='ACTIVE'")
    Slice<Long> queryGetHabitsByUserId(Long userId);

    // 유저1과 유저2의 같이 하는 습관 정보 조회
    @Query("select hf.habit from HabitFollowers hf "+
            "where hf.habit.id in (select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId1 and hf.status='ACTIVE') " +
            "and hf.follower.id = :userId2 " +
            "and hf.habit.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and hf.habit.status = 'ACTIVE' " +
            "and hf.status='ACTIVE'" +
            "order by hf.habit.id asc")
    Slice<Habit> queryGetCommonHabitsByUserId(Long userId1, Long userId2);

    // 유저1과 유저2의 같이 하는 습관 아이디 조회
    @Query("select hf.habit.id from HabitFollowers hf "+
            "where hf.habit.id in (select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId2 and hf.status='ACTIVE') " +
            "and hf.follower.id = :userId1 " +
            "and hf.habit.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and hf.habit.status = 'ACTIVE' " +
            "and hf.status='ACTIVE'" +
            "order by hf.habit.id asc")
    Slice<Long> queryGetCommonHabitIdsByUserId(Long userId1, Long userId2);

    // 유저 1의 습관 중, 유저2와 같이 하지 않는 습관 정보 조회
    @Query("select hf.habit from HabitFollowers hf "+
            "where hf.habit.id not in (select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId2 and hf.status='ACTIVE') " +
            "and hf.follower.id = :userId1 " +
            "and hf.habit.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and hf.habit.status = 'ACTIVE' " +
            "and hf.status='ACTIVE'" +
            "order by hf.habit.id asc")
    Slice<Habit> queryFindOtherHabitsByUserId(Long userId1, Long userId2);

    // 습관의 모집 여부 조회
    @Query("select case " +
            "when hf.habit.followerLimit-hf.habit.followerCount > 0 then true " +
            "else false end " +
            "from HabitFollowers hf " +
            "where hf.habit.id = :habitId")
    boolean queryGetHabitRecruitingStatus(Long habitId);

    // 습관 유저 삭제
    @Query("delete from HabitFollowers where follower.id = :userId and habit.id = :habitId ")
    Long queryDeleteFollowerById (Long userId, Long habitId);

}

