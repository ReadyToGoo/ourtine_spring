package ourtine.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.response.HabitActiveSessionGetResponse;
import ourtine.web.dto.response.HabitSessionEnterPostResponse;
import ourtine.web.dto.response.HabitSessionMvpCandidateGetResponse;

import java.util.List;
@Service
public interface HabitSessionService {

    // @Transactional
    public HabitActiveSessionGetResponse getActiveHabitSession(Long habitSessionId);

    // @Transactional
    public HabitSessionEnterPostResponse enterHabitSession(Long sessionId, User user);

    @Transactional
    public HabitSessionMvpVotePostRequestDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto);

    // @Transactional
    public List<HabitSessionMvpCandidateGetResponse> getMvpCandidateList(Long sessionId);
}
