package ourtine.service;


import ourtine.domain.User;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import org.springframework.transaction.annotation.Transactional;
import ourtine.server.web.dto.response.HabitJoinPostResponseDto;

public interface PublicHabitService {
    // 습관생성하기
    @Transactional
    public HabitCreateResponseDto createPublicHabit(HabitCreateRequestDto habitCreateRequestDto,/* MultipartFile file,*/  User user);

    // 습관 참여하기
    @Transactional
    public HabitJoinPostResponseDto joinPublicHabit(Long habitId, User user);
}
