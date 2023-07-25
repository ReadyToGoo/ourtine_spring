package ourtine.service.impl;

import ourtine.domain.PrivateHabit;
import ourtine.domain.PublicHabit;
import ourtine.domain.User;
import ourtine.repository.PrivateHabitRepository;
import ourtine.service.PrivateHabitService;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateHabitServiceImpl implements PrivateHabitService {

    private PrivateHabitRepository privateHabitRepository;

    @Override
    public HabitCreateResponseDto createPrivateHabit(HabitCreateRequestDto habitCreateRequestDto, User user) {
        // 이미지 유무 검사
        // S3 이미지 업로드
        PrivateHabit habit = PrivateHabit.builder().build();

        //Private 습관인지 검사
        if (habitCreateRequestDto.getHabitStatus()=="Private")
        {
            habit = PrivateHabit.builder()
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
            privateHabitRepository.save(habit);
            return new HabitCreateResponseDto(privateHabitRepository.save(habit).getId());
        }


        else return  null;
    }
}
