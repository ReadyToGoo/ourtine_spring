package ourtine.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.S3Uploader;
import ourtine.domain.Habit;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.enums.HabitFollowerStatus;
import ourtine.domain.mapping.HabitSessionFollower;
import ourtine.domain.mapping.UserMvp;
import ourtine.repository.*;
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
@Transactional
public class HabitSessionServiceImpl implements HabitSessionService {

    private final HabitRepository habitRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final S3Uploader s3Uploader;
    private final UserMvpRepository userMvpRepository;

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
        List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());

        List<HabitSessionFollowerResponseDto> followersResult = new ArrayList<>();
        for(User follower : followers){
            // 입장한 유저
            if ( habitSessionFollowerRepository.existsByFollowerIdAndHabitSessionId(follower.getId(),sessionId)){
                followersResult.add(new HabitSessionFollowerResponseDto(follower.getId(),follower.getNickname(),follower.getImageUrl(),
                        habitSessionFollowerRepository.findByHabitSessionIdAndFollower(sessionId,follower).get().getHabitFollowerStatus()));
            }
            // 안 한 유저
            else{
                followersResult.add(new HabitSessionFollowerResponseDto(follower.getId(),follower.getNickname(),follower.getImageUrl(),HabitFollowerStatus.NOT_ENTERED));
            }
        }

        return new HabitSessionGetResponseDto(habitSession.getId(),habit,followersResult);
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

        List<HabitSessionFollowerVoteResponseDto> result = new ArrayList<>();
        // TODO: user 정보
        followers.forEach(follower -> {
            if (follower.getHabitFollowerStatus()== HabitFollowerStatus.COMPLETE) {
                result.add(new HabitSessionFollowerVoteResponseDto(follower));
            }
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
        if (userMvpRepository.findByHabitSessionId(sessionId).isEmpty()){} // 에러 처리

        List<UserMvp> mvps = userMvpRepository.findByHabitSessionId(sessionId);
        List<HabitSessionMvpGetResponseDto> habitSessionMvpGetResponseDto = new ArrayList<>();
        mvps.forEach(mvp->{ habitSessionMvpGetResponseDto.add(
                new HabitSessionMvpGetResponseDto(mvp.getUser().getId(), mvp.getUser().getNickname(),mvp.getUser().getImageUrl()));});
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