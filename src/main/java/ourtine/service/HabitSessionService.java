package ourtine.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.response.HabitSessionGetResponseDto;
import ourtine.web.dto.response.HabitSessionEnterPostResponseDto;
import ourtine.web.dto.response.HabitSessionMvpCandidateGetResponseDto;
import ourtine.web.dto.response.HabitSessionMvpVotePostResponseDto;

@Service
public interface HabitSessionService {

    // @Transactional
    public HabitSessionGetResponseDto getHabitSession(Long habitSessionId);

    // @Transactional
    public HabitSessionEnterPostResponseDto enterHabitSession(Long sessionId, User user);

    @Transactional
    public HabitSessionMvpVotePostResponseDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto);

    // @Transactional
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(Long sessionId);
}
