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

    // 습관 인증샷 올리기
    @Override
    public HabitSessionUploadVideoPostResponseDto uploadVideo(Long sessionId, MultipartFile file, User user) throws IOException {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        HabitSessionFollower habitSessionFollower = habitSessionFollowerRepository.queryGetHabitSessionFollower(user.getId(),habitSession.getId()).orElseThrow();

        if (file.isEmpty()){} // TODO: 에러 처리
        // 영상 업로드
        String videoUrl = s3Uploader.upload(file,"");
        // 유저의 세션 완료 처리 & 세션 완료 처리
        habitSessionFollower.uploadVideo(videoUrl);
        return new HabitSessionUploadVideoPostResponseDto(habitSession.getId(),user.getId());
    }

    // 습관 세션 MVP 후보 조회
    @Override
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(Long sessionId) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        List<HabitSessionFollower> followers = habitSessionFollowerRepository.queryGetHabitSessionFollowers(sessionId);

        List<HabitSessionFollowerGetResponseDto> result = new ArrayList<>();
        // TODO: user 정보
        followers.forEach(follower -> {
            result.add(new HabitSessionFollowerGetResponseDto(follower.getId(),"닉네임","이미지",follower.getVideoUrl(),
                    habitSessionFollowerRepository.queryGetHabitSessionFollowerCompleteStatus(follower.getId(),habitSession.getId())));
        });
      return  new HabitSessionMvpCandidateGetResponseDto(habitSession.getId(),result) ;
    }

    // 습관 세션 mvp 투표하기
    @Override
    public HabitSessionMvpVotePostResponseDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto) {
        HabitSession habitSession = habitSessionRepository.findById(sessionId).orElseThrow();
        HabitSessionFollower mySession = habitSessionFollowerRepository.queryGetHabitSessionFollower(user.getId(), habitSession.getId()).orElseThrow();
        HabitSessionFollower mvpUser = habitSessionFollowerRepository.queryGetHabitSessionFollower(user.getId(), sessionId).orElseThrow();

        mySession.voteMvp(mvpUser.getId());

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
        long result=0L; // 최다 득표수 저장
        List<Long> mvpId = new ArrayList<>(); // 베스트 습관러 저장

        for(int i = 0; i<followers.size(); i++){
            voteNum[i] = Collections.frequency(votes, followers.get(i));
            resultMap.put(followers.get(i),voteNum[i]);
            if (result <= voteNum[i]) {
                result = voteNum[i];
            }
        }
        for (Long follower : followers) {
            if (resultMap.get(follower) == result) { // 최다 득표수와 유저의 득표수가 같다면 유저 아이디 저장
                mvpId.add(follower);
            }
        }
        List<HabitSessionMvpGetResponseDto> habitSessionMvpGetResponseDto = new ArrayList<>();
        mvpId.forEach(mvp->{ habitSessionMvpGetResponseDto.add(
                new HabitSessionMvpGetResponseDto(mvp,"닉네임","이미지"));});
        return habitSessionMvpGetResponseDto;
    }

}