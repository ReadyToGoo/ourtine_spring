package ourtine.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.repository.UserRepository;
import ourtine.service.impl.HabitSessionServiceImpl;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/test")
public class HabitSessionTestController {
    private final HabitSessionServiceImpl habitSessionService;
    private final UserRepository userRepository;

    public HabitSessionTestController(HabitSessionServiceImpl habitSessionService, UserRepository userRepository) {
        this.habitSessionService = habitSessionService;
        this.userRepository = userRepository;
    }

    // 세션 입장하기
    @PostMapping("/habits/{habit_id}/habit-sessions")
    public HabitSessionEnterPostResponseDto enterHabitSession(@PathVariable Long habit_id){
        User dana = userRepository.findById(1l).get();
        return habitSessionService.enterHabitSession(habit_id, dana);
    }

    // 세션 정보 조회
    @GetMapping("/habit-sessions/{session_id}")
    public HabitSessionGetResponseDto getHabitSession(@PathVariable Long session_id){
        User dana = userRepository.findById(1l).get();
        return habitSessionService.getHabitSession(session_id,dana);
    }

    // 습관 인증샷 올리기
    @PostMapping("/habit-sessions/{session_id}/upload")
    public HabitSessionUploadVideoPostResponseDto uploadVideo(@PathVariable Long session_id, @RequestPart MultipartFile file) throws IOException {
        User dana = userRepository.findById(1l).get();
        if (file.isEmpty()){} // TODO: 에러 처리
        return habitSessionService.uploadVideo(session_id, file, dana);
    }

    // 베스트 습관러 후보 조회
    @GetMapping("/habit-sessions/{session_id}/vote")
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(@PathVariable Long session_id){
        User dana = userRepository.findById(1l).get();
        return habitSessionService.getMvpCandidateList(session_id);
    }

    // 베스트 습관러 투표하기
    @PostMapping("/habit-sessions/{session_id}/vote")
    public HabitSessionMvpVotePostResponseDto voteMvp(@PathVariable Long session_id,@RequestBody HabitSessionMvpVotePostRequestDto requestDto){
        User dana = userRepository.findById(1l).get();
        return habitSessionService.voteMvp(session_id, dana, requestDto);
    }

    // 베스트 습관러 결과 보여주기
    @GetMapping("/habit-sessions/{session_id}/mvp")
    public List<HabitSessionMvpGetResponseDto> showMvp(@PathVariable Long session_id){
        User dana = userRepository.findById(1l).get();
        return habitSessionService.showMvp(session_id, dana);
    }

    // 습관 회고 쓰기
    @PatchMapping("/habit-sessions/{session_id}/review")
    public HabitSessionReviewPostResponseDto writeReview(@PathVariable Long session_id, @RequestBody HabitSessionReviewPostRequestDto requestDto)
    {
        User dana = userRepository.findById(1l).get();
        return habitSessionService. writeReview(session_id,requestDto,dana);
    }

}
