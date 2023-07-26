package ourtine.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.*;
import ourtine.domain.mapping.HabitDays;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.HabitHashtag;
import ourtine.repository.*;
import ourtine.service.PrivateHabitService;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateHabitServiceImpl implements PrivateHabitService {

    private final PrivateHabitRepository privateHabitRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;
    private final HabitHashtagRepository habitHashtagRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitDaysRepository habitDaysRepository;

    @Override
    public HabitCreateResponseDto createPrivateHabit(HabitCreateRequestDto habitCreateRequestDto, User user) {
        // 이미지 유무 검사
        // S3 이미지 업로드
        PrivateHabit habit = PrivateHabit.builder().build();

        //Private 습관인지 검사
        if (habitCreateRequestDto.getHabitStatus()=="Private")
        {
            Category category = categoryRepository.findByName(habitCreateRequestDto.getCategory()).orElseThrow();
            habit = PrivateHabit.builder()
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
            privateHabitRepository.save(habit);

            // 요일 매핑테이블에 저장
            Habit savedHabit = privateHabitRepository.save(habit);
            habitCreateRequestDto.getDays().forEach(name ->{
                HabitDays habitDays = HabitDays.builder().habit(savedHabit).day(name).build();
                habitDaysRepository.save(habitDays);
            });
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

            return new HabitCreateResponseDto(privateHabitRepository.save(habit).getId());
        }


        else return  null;
    }
}
