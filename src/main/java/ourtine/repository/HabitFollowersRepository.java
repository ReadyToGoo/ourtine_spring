package ourtine.repository;

import ourtine.domain.User;
import ourtine.domain.enums.Status;
import ourtine.domain.mapping.HabitFollowers;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface HabitFollowersRepository extends JpaRepository<HabitFollowers,Long> {

    // 습관 팔로워 정보 조회
    @Query("select hf.follower from HabitFollowers hf where hf.habit.id = :habitId and hf.status='ACTIVE'")
    Slice<User> queryFindHabitFollowers(Long habitId);

    // 참여하고 있는 습관 조회
    @Query("select hf.habit.id from HabitFollowers hf where hf.follower.id = :userId and hf.status='ACTIVE'")
    List<Long> queryFindMyFollowingHabits(Long userId);

}

