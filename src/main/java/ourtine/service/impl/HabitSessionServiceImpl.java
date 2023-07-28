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
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.response.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitSessionServiceImpl implements HabitSessionService {

    private final HabitRepository habitRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final HabitSessionRepository habitSessionRepository;

/*    // 스케쥴러 사용해서 수정하기
    // 습관 세션 생성하기
    @Override
    public Long createHabitSession() {

        HabitSession habitSession = HabitSession.builder().habit(habit).date(date).build();
        habitSessionRepository.save(habitSession);
        return habitSessionRepository.save(habitSession).getId();
    }*/

    // 활성화 된 습관 세션 정보 조회
    @Override
    public HabitSessionGetResponseDto getHabitSession(Long habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        HabitSession habitSession = habitSessionRepository.queryFindTodaySessionByHabitId(habitId).orElseThrow();
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);

        List<HabitSessionFollowersGetResponseDto> habitSessionFollowers
                = followers.map(user ->
                new HabitSessionFollowersGetResponseDto(user.getId(), user.getNickname(), user.getImageUrl(),
                        habitSessionFollowerRepository.queryExistsByUserIdAndHabitSessionId(user.getId(),habitSession.getId()))).toList();
        return new HabitSessionGetResponseDto(habitSession.getId(),habit,habitSessionFollowers);
    }

    // 습관 세션 입장하기
    @Override
    public HabitSessionEnterPostResponseDto enterHabitSession(Long habitId, User user) {
        HabitSession habitSession = habitSessionRepository.queryFindTodaySessionByHabitId(habitId).orElseThrow();
        HabitSessionFollower habitSessionFollower = HabitSessionFollower.builder()
                        .follower(user).habitSession(habitSession).build();
        habitSessionFollowerRepository.save(habitSessionFollower);
        return new HabitSessionEnterPostResponseDto(habitSession,user);
    }
    // 습관 세션 mvp 투표하기
    @Override
    public HabitSessionMvpVotePostResponseDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        HabitSessionFollower habitSessionFollower = habitSessionFollowerRepository.queryGetHabitSessionFollower(user.getId(), habitSession.getId()).orElseThrow();
        habitSessionFollower.voteMvp(habitSessionMvpVotePostRequestDto.getMvpVote());

        return new HabitSessionMvpVotePostResponseDto(habitSessionMvpVotePostRequestDto.getMvpVote());
    }

    // 습관 세션 MVP 후보 조회
    @Override
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(Long sessionId) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        List<HabitSessionFollower> followers = habitSessionFollowerRepository.queryGetHabitSessionFollowers(sessionId);

        List<HabitSessionFollowerGetResponseDto> result = new ArrayList<>();
        // TODO: user 정보
        followers.forEach(follower -> {
            result.add(new HabitSessionFollowerGetResponseDto(follower.getId(),"닉네임","이미지","영상",
                    habitSessionFollowerRepository.queryGetHabitSessionFollowerCompleteStatus(follower.getId(),habitSession.getId())));
        });
      return  new HabitSessionMvpCandidateGetResponseDto(habitSession.getId(),result) ;
    }

}
