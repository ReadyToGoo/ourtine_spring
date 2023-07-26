package ourtine.service.impl;

import ourtine.domain.*;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.HabitHashtag;
import ourtine.repository.*;
import ourtine.server.web.dto.response.HabitJoinPostResponseDto;
import ourtine.service.PublicHabitService;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PublicHabitServiceImpl implements PublicHabitService {

    private final PublicHabitRepository publicHabitRepository;
    private final CategoryRepository categoryRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HashtagRepository hashtagRepository;
    private final HabitHashtagRepository habitHashtagRepository;

    @Override
    public HabitCreateResponseDto createPublicHabit(HabitCreateRequestDto habitCreateRequestDto, /*MultipartFile file, */User user) {

        // 이미지 유무 검사

        // S3 이미지 업로드

        //Public 습관인지 검사
        PublicHabit habit = PublicHabit.builder().build();
        if (habitCreateRequestDto.getHabitStatus()=="Public")
        {
            Category category = categoryRepository.findByName(habitCreateRequestDto.getCategory()).orElseThrow();
            habit = PublicHabit.builder()
                    .host(user)
                    .title(habitCreateRequestDto.getTitle())
                    .detail(habitCreateRequestDto.getDetail())
                    .imageUrl("이미지")
                    .categoryId(category.getId())
                    .startTime(habitCreateRequestDto.getStartTime())
                    .endTime(habitCreateRequestDto.getEndTime())
                    .startDate(habitCreateRequestDto.getStartDate())
                    .endDate(habitCreateRequestDto.getEndDate())
                    .followerLimit(habitCreateRequestDto.getFollowerLimit())
                    .build();

            Habit savedHabit = publicHabitRepository.save(habit);
            // 해시태그 DB에 저장
            habitCreateRequestDto.getHashtags().forEach(name->{
                boolean index = false;
                Hashtag hashtag = Hashtag.builder().name(name).build();
                if (!index)
                {hashtagRepository.save(hashtag);
                    index =true;}
                // 해시태그 매핑테이블에 저장
                HabitHashtag habitHashtag = HabitHashtag.builder().habit(savedHabit).hashtag(hashtag).build();
                habitHashtagRepository.save(habitHashtag);
            });

            // 팔로워 매핑테이블에 호스트 저장
            HabitFollowers habitFollowers = HabitFollowers.builder().follower(user).habit(habit).build();
            habitFollowersRepository.save(habitFollowers);

        }

        return new HabitCreateResponseDto(publicHabitRepository.save(habit).getId());
    }

    @Override
    public HabitJoinPostResponseDto joinPublicHabit(Long habitId, User user) {
        return null;
    }

}