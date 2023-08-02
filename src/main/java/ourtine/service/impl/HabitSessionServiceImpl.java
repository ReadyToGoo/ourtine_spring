package ourtine.service.impl;

import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.S3Uploader;
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
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitSessionServiceImpl implements HabitSessionService {

    private final HabitRepository habitRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final S3Uploader s3Uploader;

    // 습관 세션 입장하기
    @Override
    public HabitSessionEnterPostResponseDto enterHabitSession(Long habitId, User user) {
        if (habitRepository.findById(habitId).isEmpty()){} // 에러 처리
        HabitSession habitSession = habitSessionRepository.queryFindTodaySessionByHabitId(habitId).orElseThrow(); // 에러 처기

        HabitSessionFollower habitSessionFollower = HabitSessionFollower.builder()
                .follower(user).habitSession(habitSession).build();
        habitSessionFollowerRepository.save(habitSessionFollower);
        return new HabitSessionEnterPostResponseDto(habitSession,user);
    }

    // 활성화 된 습관 세션 정보 조회
    @Override
    public HabitSessionGetResponseDto getHabitSession(Long sessionId, User user) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow(); //에러처리

        Habit habit = habitSession.getHabit();
        Slice<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());

        List<HabitSessionFollowersGetResponseDto> habitSessionFollowers
                = followers.map(follower ->
                new HabitSessionFollowersGetResponseDto(follower.getId(), follower.getNickname(), follower.getImageUrl(),
                        habitSessionFollowerRepository.existsByFollowerIdAndHabitSessionId(follower.getId(),sessionId))).toList();
        return new HabitSessionGetResponseDto(habitSession.getId(),habit,habitSessionFollowers);
    }


    // 습관 인증샷 올리기
    @Override
    public HabitSessionUploadVideoPostResponseDto uploadVideo(Long sessionId, MultipartFile file, User user) throws IOException {
        if (habitSessionRepository.findById(sessionId).isEmpty()){}
        HabitSessionFollower habitSessionFollower = habitSessionFollowerRepository.findByHabitSessionIdAndFollower(sessionId,user).orElseThrow();

        // 영상 업로드
        String videoUrl = s3Uploader.upload(file,"");
        // 유저의 세션 완료 처리
        habitSessionFollower.uploadVideo(videoUrl);
        return new HabitSessionUploadVideoPostResponseDto(sessionId,user.getId());
    }

    // 베스트 습관러 후보 조회
    @Override
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(Long sessionId) {
        if (habitSessionRepository.findById(sessionId).isEmpty()){} // 에러 처리
        List<HabitSessionFollower> followers = habitSessionFollowerRepository.findByHabitSession_Id(sessionId).getContent();

        List<HabitSessionFollowerGetResponseDto> result = new ArrayList<>();
        // TODO: user 정보
        followers.forEach(follower -> {
            result.add(new HabitSessionFollowerGetResponseDto(follower.getId(),"닉네임","이미지",follower.getVideoUrl(),
                    habitSessionFollowerRepository.queryGetHabitSessionFollowerCompleteStatus(follower.getId(),sessionId)));
        });
      return  new HabitSessionMvpCandidateGetResponseDto(sessionId,result) ;
    }

    // 베스트 습관러 투표하기
    @Override
    public HabitSessionMvpVotePostResponseDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto) {
        HabitSessionFollower mySession = habitSessionFollowerRepository.findByHabitSessionIdAndFollower(sessionId,user).orElseThrow();
        if (!habitSessionFollowerRepository.existsByFollowerIdAndHabitSessionId(
                habitSessionMvpVotePostRequestDto.getMvpVote(),sessionId)){} // 에러 처리

        mySession.voteMvp(habitSessionMvpVotePostRequestDto.getMvpVote());

        return new HabitSessionMvpVotePostResponseDto(habitSessionMvpVotePostRequestDto.getMvpVote());
    }

    // 투표 결과 보여주기
    @Override
    public List<HabitSessionMvpGetResponseDto> showMvp(Long sessionId, User user) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        List<Long> followers = habitFollowersRepository.queryFindHabitFollowerIds(habitSession.getHabit());
        List<Long> votes = habitSessionFollowerRepository.queryGetHabitSessionVotes(sessionId);

        long [] voteNum = new long[followers.size()]; // 득표 수 저장
        HashMap<Long,Long> resultMap = new HashMap<>(); // ( 유저 아이디, 득표 수)
        long result = 0L; // 최다 득표수
        List<Long> mvp = new ArrayList<>(); // 베스트 습관러

        for(int i = 0; i<followers.size(); i++){
            voteNum[i] = Collections.frequency(votes, followers.get(i));
            resultMap.put(followers.get(i),voteNum[i]);
            if (result <= voteNum[i]) {
                result = voteNum[i];
            }
        }
        for (Long follower : followers) {
            if (resultMap.get(follower) == result) { // 최다 득표수와 유저의 득표수가 같다면 유저 아이디 저장
                mvp.add(follower);
            }
        }

        List<HabitSessionMvpGetResponseDto> habitSessionMvpGetResponseDto = new ArrayList<>();
        mvp.forEach(mvpId->{ habitSessionMvpGetResponseDto.add(
                new HabitSessionMvpGetResponseDto(mvpId,"닉네임","이미지"));});
        return habitSessionMvpGetResponseDto;
    }

    // 습관 회고 쓰기
    @Override
    public HabitSessionReviewPostResponseDto writeReview(Long sessionId, HabitSessionReviewPostRequestDto requestDto, User user) {
        if (habitSessionRepository.findById(sessionId).isEmpty()){} //에러 처리
        HabitSessionFollower habitSessionFollower = habitSessionFollowerRepository.findByHabitSessionIdAndFollower(sessionId,user).orElseThrow();
        habitSessionFollower.writeReview(requestDto.getStarRate(), requestDto.getEmotion());
        return new HabitSessionReviewPostResponseDto(user.getId(),sessionId);
    }

}