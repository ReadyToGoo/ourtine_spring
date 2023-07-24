package ourtine.service.impl;

import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
import ourtine.domain.User;
import ourtine.repository.*;
import ourtine.service.HabitService;
import ourtine.server.web.dto.response.HabitFollowersGetResponseDto;
import ourtine.server.web.dto.response.HabitFollowingListGetResponseDto;
import ourtine.server.web.dto.response.HabitGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;

    // 습관 상세 정보조회 (참여 x)
    @Override
    public HabitGetResponseDto getHabit(long habitId, User user) {

        // 에러 처리 필요
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Category category  = categoryRepository.findById(habit.getCategoryId()).get();
        List<Hashtag> hashtags= hashtagRepository.queryFindHabitHashtag(habitId);
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);

        List<HabitFollowersGetResponseDto> habitFollowersResult
                = followers.map(u->new HabitFollowersGetResponseDto(
                    u.getId(), u.getNickname(), u.getImageUrl()
        )).toList();
        HabitGetResponseDto habitGetResponseDto = new HabitGetResponseDto(habit,hashtags,category,habitFollowersResult);

        return habitGetResponseDto;
    }

    @Override
    public Slice<HabitFollowingListGetResponseDto> getFollowingHabits(User user) {
        return null;
    }


}
