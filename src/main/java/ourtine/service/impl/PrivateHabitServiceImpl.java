package ourtine.service.impl;

import org.springframework.transaction.annotation.Transactional;
import ourtine.repository.*;
import ourtine.service.PrivateHabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
