package ourtine.service.impl;

import ourtine.domain.PublicHabit;
import ourtine.domain.User;
import ourtine.repository.PublicHabitRepository;
import ourtine.service.PublicHabitService;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicHabitServiceImpl implements PublicHabitService {

    private final PublicHabitRepository publicHabitRepository;

    @Override
    public HabitCreateResponseDto createPublicHabit(HabitCreateRequestDto habitCreateRequestDto, /*MultipartFile file, */User user) {

        // 이미지 유무 검사

        // S3 이미지 업로드

        //Public 습관인지 검사
        PublicHabit habit = PublicHabit.builder().build();
        if (habitCreateRequestDto.getHabitStatus()=="Public")
        {
            habit = PublicHabit.builder()
                    .host(user)
                    .title(habitCreateRequestDto.getTitle())
                    .detail(habitCreateRequestDto.getDetail())
                    .imageUrl("이미지")
                    .categoryId(habitCreateRequestDto.getCategoryId())
                    .startTime(habitCreateRequestDto.getStartTime())
                    .endTime(habitCreateRequestDto.getEndTime())
                    .startDate(habitCreateRequestDto.getStartDate())
                    .endDate(habitCreateRequestDto.getEndDate())
                    .followerLimit(habitCreateRequestDto.getFollowerLimit())
                    .build();
            publicHabitRepository.save(habit);
        }

        return new HabitCreateResponseDto(publicHabitRepository.save(habit).getId());
    }

}