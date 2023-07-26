package ourtine.service.impl;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Habit;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.mapping.HabitSessionFollower;
import ourtine.repository.HabitFollowersRepository;
import ourtine.repository.HabitRepository;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;
import ourtine.service.HabitSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ourtine.server.web.dto.request.HabitVoteMvpRequest;
import ourtine.server.web.dto.response.HabitActiveSessionGetResponse;
import ourtine.server.web.dto.response.HabitSessionFollowersGetResponseDto;
import ourtine.server.web.dto.response.HabitSessionGetMvpCandidateResponse;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitSessionServiceImpl implements HabitSessionService {

    private final HabitRepository habitRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final HabitSessionRepository habitSessionRepository;

    // 스케쥴러 사용해서 수정하기
    // 습관 세션 생성하기
    @Override
    public Long createHabitSession(Long habitId, User user) {
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Date date = new Date(); // 현재 날짜

        HabitSession habitSession = HabitSession.builder().habit(habit).date(date).build();
        habitSessionRepository.save(habitSession);
        return habitSessionRepository.save(habitSession).getId();
    }

    // 활성화 된 습관 세션 정보 조회
    @Override
    public HabitActiveSessionGetResponse getActiveHabitSession(Long habitSessionId) {
        Long habitId = habitSessionRepository.queryFindHabitIdBySessionId(habitSessionId);
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);

        List<HabitSessionFollowersGetResponseDto> habitSessionFollowers
                = followers.map(user ->
                new HabitSessionFollowersGetResponseDto(user.getId(), user.getNickname(), user.getImageUrl(),
                        habitSessionFollowerRepository.queryExistsByUserIdAndHabitSessionId(user.getId(),habitSessionId))).toList();
        return new HabitActiveSessionGetResponse(habitSessionId,habit.getTitle(),habit.getStartTime(),habit.getEndTime(),habitSessionFollowers);
    }

    // 습관 세션 입장하기
    @Override
    public Long enterHabitSession(Long sessionId, User user) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        HabitSessionFollower habitSessionFollower = HabitSessionFollower.builder()
                        .follower(user).habitSession(habitSession).build();
        habitSessionFollowerRepository.save(habitSessionFollower);
        return habitSessionFollowerRepository.save(habitSessionFollower).getId();
    }
    // 습관 세션 mvp 투표하기
    @Override
    public Long voteMvp(Long sessionId, User user, HabitVoteMvpRequest habitVoteMvpRequest) {
        HabitSessionFollower habitSessionFollower = habitSessionFollowerRepository.queryGetHabitSessionFollower(user.getId(), sessionId);
        habitSessionFollower.voteMvp(habitVoteMvpRequest.getMvpVote());

        return habitVoteMvpRequest.getMvpVote();
    }

    // 습관 세션 MVP 후보 조회
    @Override
    public List<HabitSessionGetMvpCandidateResponse> getMvpCandidateList(Long sessionId) {
      Long habitId = habitSessionRepository.findById(sessionId).orElseThrow().getHabit().getId();
      return  null;
    }

}
