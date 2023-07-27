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

}
