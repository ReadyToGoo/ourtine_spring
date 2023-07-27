package ourtine.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.*;
import ourtine.domain.enums.Day;
import ourtine.domain.mapping.HabitDays;
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
@Transactional(readOnly = true)
public class PublicHabitServiceImpl implements PublicHabitService {

    private final PublicHabitRepository publicHabitRepository;
    private final CategoryRepository categoryRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HashtagRepository hashtagRepository;
    private final HabitHashtagRepository habitHashtagRepository;
    private final HabitDaysRepository habitDaysRepository;


}