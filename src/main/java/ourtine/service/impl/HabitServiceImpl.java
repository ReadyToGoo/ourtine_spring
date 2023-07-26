package ourtine.service.impl;

import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
import ourtine.domain.User;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.Sort;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.repository.*;
import ourtine.server.web.dto.response.*;
import ourtine.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final PublicHabitRepository publicHabitRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitDaysRepository habitDaysRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;

    // 습관 참여 여부
    @Override
    public boolean getUserIsHabitFollower(Long habitSessionId, User user) {
        Long habitId = habitSessionRepository.queryFindHabitIdBySessionId(habitSessionId);
        return habitFollowersRepository.queryExistsByUserIdAndHabitId(habitId,user.getId());
    }

    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)
    @Override
    public Slice<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user) {
        int dayNum = LocalDate.now(ZoneId.of("Asia/Seoul")).getDayOfWeek().getValue();
        Day day = null;
        switch (dayNum) {
            case 1 : day = Day.MON;break;
            case 2 : day = Day.TUE;break;
            case 3 : day = Day.WED;break;
            case 4 : day = Day.THU;break;
            case 5 : day = Day.FRI;break;
            case 6 : day = Day.SAT;break;
            case 7 : day = Day.SUN;break;
        }

        Slice<Long> followingHabitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(user.getId());
        List<Long> habitIdsOfDay = habitDaysRepository.queryFindMyHabitsByDay(followingHabitIds.toList(),day);
        Slice<Habit> habitsOfDay = habitRepository.queryFindHabitsById(habitIdsOfDay);
        return habitsOfDay.map(habit -> new HabitMyFollowingListGetResponseDto(
                habit,
                habitSessionRepository.queryFindTodaySessionIdByHabitId(habit.getId()),
                habitSessionFollowerRepository.queryGetHabitSessionFollowerCompleteStatus(user.getId(),habit.getId())));

    }

    // 습관 상세 정보조회 (참여 x)
    @Override
    public HabitNotFollowingGetResponseDto getNotFollowingHabit(Long habitId, User user) {

        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Category category  = categoryRepository.findById(habit.getCategoryId()).orElseThrow();
        List<String> hashtags= hashtagRepository.queryFindHabitHashtag(habitId);
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);

        List<HabitFollowersGetResponseDto> habitFollowersResult =
                followers.map(follower->new HabitFollowersGetResponseDto(
                follower.getId(), follower.getNickname(), follower.getImageUrl(),
                habitFollowersRepository.queryExistsByUserIdAndHabitId(habitId,follower.getId()))).toList();
        return new HabitNotFollowingGetResponseDto(habit,hashtags,category,habitFollowersResult);

    }

    // 습관 상세 정보조회 (참여 O)
    @Override
    public HabitFollowingGetResponseDto getFollowingHabit(Long habitId, User user) {
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Category category  = categoryRepository.findById(habit.getCategoryId()).get();
        List<String> hashtags= hashtagRepository.queryFindHabitHashtag(habitId);
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);

        List<HabitFollowersGetResponseDto> habitFollowersResult =
                followers.map(follower->new HabitFollowersGetResponseDto(
                        follower.getId(), follower.getNickname(), follower.getImageUrl(),
                        habitFollowersRepository.queryExistsByUserIdAndHabitId(habitId,follower.getId()))).toList();
        return new HabitFollowingGetResponseDto(habit,hashtags,category,habitFollowersResult);
    }


    // 친구 프로필 - 팔로잉 하는 습관 목록
    @Override
    public HabitFriendFollowingListGetResponse getFriendFollowingHabits(User friend, User me) {
       Slice<Habit> commonHabits = habitFollowersRepository.queryGetCommonHabitsByUserId(friend.getId(),me.getId());
       Slice<Habit> otherHabits = habitFollowersRepository.queryFindOtherHabitsByUserId(friend.getId(),me.getId());
       List<HabitFollowingInfoDto> commonHabitsInfo = commonHabits.map(HabitFollowingInfoDto::new).toList();
       List<HabitFollowingInfoDto> otherHabitsInfo = otherHabits.map(HabitFollowingInfoDto::new).toList();
       return new HabitFriendFollowingListGetResponse(commonHabitsInfo,otherHabitsInfo);
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @Override
    public HabitUserFollowingListGetResponse getUserFollowingHabits(User user) {
        List<Long> habitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(user.getId()).toList();
        Slice<Habit> habits = habitRepository.queryFindHabitsById(habitIds);
        List<HabitFollowingInfoDto> result = habits.map(HabitFollowingInfoDto::new).toList();
        return new HabitUserFollowingListGetResponse(result);
    }

    // 추천 습관 목록
    @Override
    public Slice<HabitRecommendListResponseDto> getRecommendHabits(User user) {
        Slice<Habit> habits = habitRepository.queryGetRecommendHabits(user.getId());
        Slice<HabitRecommendListResponseDto> result = habits.map(habit ->
                new HabitRecommendListResponseDto(habit,categoryRepository.findById(habit.getCategoryId()).get(),habit.getHost()));
        return result;
    }

    // 습관 참여하기
    @Override
    public HabitJoinPostResponseDto joinHabit(Long habitId, User user) {
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        HabitFollowers habitFollower = HabitFollowers.builder().follower(user).habit(habit).build();
        habitFollowersRepository.save(habitFollower);
        return new HabitJoinPostResponseDto(habitId, user.getId());
    }

    // 습관 검색하기
    @Override
    public Slice<HabitSearchResponseDto> searchHabits(Sort sort, User user, String keyword) {
        if (sort == Sort.CREATED_DATE){
            Slice<Habit> habits = habitRepository.queryFindHabitOrderByCreatedAt(user.getId(), keyword);
            return habits.map(habit -> new HabitSearchResponseDto(
                    habit,
                    habitFollowersRepository.queryGetHabitRecruitingStatus(habit.getId()),
                    hashtagRepository.queryFindHabitHashtag(habit.getId()),
                    categoryRepository.findById(habit.getCategoryId()).orElseThrow()
            ));
        }
        else if (sort == Sort.START_DATE){
            Slice<Habit> habits = habitRepository.queryFindHabitOrderByStartDate(user.getId(), keyword);
            return habits.map(habit -> new HabitSearchResponseDto(
                    habit,
                    habitFollowersRepository.queryGetHabitRecruitingStatus(habit.getId()),
                    hashtagRepository.queryFindHabitHashtag(habit.getId()),
                    categoryRepository.findById(habit.getCategoryId()).orElseThrow()
            ));
        }
        else if (sort == Sort.RECRUITING){
            Slice<Habit> habits = habitRepository.querySearchFindOrderByFollowerCount(user.getId(), keyword);
            return habits.map(habit -> new HabitSearchResponseDto(
                    habit,
                    habitFollowersRepository.queryGetHabitRecruitingStatus(habit.getId()),
                    hashtagRepository.queryFindHabitHashtag(habit.getId()),
                    categoryRepository.findById(habit.getCategoryId()).orElseThrow()
            ));
        }
        else
            //에러 처리 해야함
            return null;
    }

    // 습관 탈퇴하기
    @Override
    public HabitFollowerResponseDto quitHabit(Long habitId, User user) {
        habitFollowersRepository.queryDeleteFollowerById(habitId,user.getId());
        return new HabitFollowerResponseDto(habitId,user.getId());
    }

}
