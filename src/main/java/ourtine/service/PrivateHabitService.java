package ourtine.service;

import ourtine.domain.User;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.HabitCreateResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface PrivateHabitService {
    @Transactional
    public HabitCreateResponseDto createPrivateHabit(HabitCreateRequestDto habitCreateRequestDto,/* MultipartFile file,*/  User user);

}
