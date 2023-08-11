package ourtine.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ourtine.repository.*;
import ourtine.service.PublicHabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicHabitServiceImpl implements PublicHabitService {

    private final CategoryRepository categoryRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HashtagRepository hashtagRepository;
    private final HabitHashtagRepository habitHashtagRepository;
    private final HabitDaysRepository habitDaysRepository;


}