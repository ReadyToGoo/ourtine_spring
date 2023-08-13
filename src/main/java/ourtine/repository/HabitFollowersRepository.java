package ourtine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.mapping.HabitFollowers;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


@Repository
public interface HabitFollowersRepository extends JpaRepository<HabitFollowers,Long> {


    // 습관 참여 여부
    Optional<HabitFollowers> findByHabitIdAndFollowerId(Long habitId, Long userId);

    // 습관의 팔로워 수 조회
    @Query("select count (hf) from HabitFollowers hf where hf.habit = :habit " +
            "and hf.status='ACTIVE'" )
    long countHabitFollowersByHabit(Habit habit);

    // 습관 팔로워 정보 조회
    @Query("select hf.follower from HabitFollowers hf " +
            "where hf.habit.id = :habitId " +
            "and hf.status='ACTIVE' " +
            "and hf.habit.status = 'ACTIVE'")
    List<User> queryFindHabitFollowers(Long habitId);

    // 습관 팔로워 정보 조회
    @Query("select hf.follower from HabitFollowers hf " +
            "where hf.habit = :habit and hf.status='ACTIVE' " +
            "and hf.habit.status = 'ACTIVE'")
    List<User> queryFindHabitFollowerIds(Habit habit);

    // 참여하고 있는 습관 아이디 조회
    @Query("select hf.habit.id from HabitFollowers hf " +
            "where hf.follower.id = :userId " +
            "and hf.habit.endDate >= curdate() ")
    Slice<Long> queryFindMyFollowingHabitIds(Long userId, Pageable pageable);

    // 참여한 습관 조회
    @Query("select hf.habit.id from HabitFollowers hf " +
            "where hf.follower.id = :userId " +
            "and hf.status='ACTIVE' " )
    Slice<Long> queryFindMyHabitIds(Long userId, Pageable pageable);

    // 참여했던 습관 아이디 조회
    @Query("select hf.habit.id from HabitFollowers hf " +
            "where hf.follower.id = :userId " +
            "and hf.status='ACTIVE' " +
            "and hf.habit.endDate < curdate() " )
    Slice<Long> queryFindMyFollowedHabitIds(Long userId, Pageable pageable);

    // 유저1과 유저2의 같이 하는 습관 정보 조회
    @Query("select hf.habit from HabitFollowers hf "+
            "where hf.habit.id in (select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId and hf.status='ACTIVE') " +
            "and hf.follower = :me " +
            "and hf.habit.host not in (select b.blocked from Block b where b.blocker = :me) " + // 호스트가 내가 차단한 유저인지 필터링
            "and hf.habit.host not in (select b.blocker from Block b where b.blocked = :me) " +
            "and hf.habit.endDate >= CURDATE() " + // 종료 된 습관 필터링
            "and hf.habit.status = 'ACTIVE' " +
            "and hf.status='ACTIVE'" +
            "order by hf.habit.id asc")
    Slice<Habit> queryGetCommonHabitsByUserId(Long userId, User me);

    // 유저 1의 습관 중, 유저2와 같이 하지 않는 습관 정보 조회
    @Query("select hf.habit from HabitFollowers hf "+
            "where hf.habit.id not in (select hf.habit.id from HabitFollowers hf where hf.follower = :me and hf.status='ACTIVE') " +
            "and hf.follower.id = :userId " +
            "and hf.habit.host not in (select b.blocked from Block b where b.blocker = :me) " + // 호스트가 내가 차단한 유저인지 필터링
            "and hf.habit.host not in (select b.blocker from Block b where b.blocked = :me) " +
            "and hf.habit.endDate >= CURDATE() " + // 종료 되지 않은 습관 필터링
            "and hf.habit.status = 'ACTIVE' " +
            "and hf.status='ACTIVE'" +
            "order by hf.habit.id asc")
    Slice<Habit> queryFindOtherHabitsByUserId(Long userId, User me, Pageable pageable);


    // 습관 유저 삭제
    @Modifying
    @Transactional
    void deleteByFollowerAndHabit_Id (User follower, Long habit_id);

    // 습관 아이디로 삭제
    @Transactional
    void deleteByHabitId(Long habitId);

}

