package ourtine.service;

import ourtine.domain.Habit;
import ourtine.domain.User;
import org.springframework.transaction.annotation.Transactional;
import ourtine.server.web.dto.request.HabitVoteMvpRequest;
import ourtine.server.web.dto.response.HabitActiveSessionGetResponse;
import ourtine.server.web.dto.response.HabitSessionGetMvpCandidateResponse;

import java.util.List;

public interface HabitSessionService {
    // @Transactional
    public Long createHabitSession(Long habitId, User user);

    // @Transactional
    public HabitActiveSessionGetResponse getActiveHabitSession(Long habitSessionId);

    // @Transactional
    public Long enterHabitSession(Long sessionId, User user);

    // @Transactional
    public Long voteMvp(Long sessionId, User user, HabitVoteMvpRequest habitVoteMvpRequest);

    // @Transactional
    public List<HabitSessionGetMvpCandidateResponse> getMvpCandidateList(Long sessionId);
}
