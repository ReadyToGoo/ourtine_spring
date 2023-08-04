package ourtine.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.service.UserService;
import ourtine.service.impl.HabitSessionServiceImpl;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HabitSessionController {
    private final HabitSessionServiceImpl habitSessionService;
    private final UserService userService;

//    public HabitSessionController(HabitSessionServiceImpl habitSessionService) {
//        this.habitSessionService = habitSessionService;
//    }

    // 세션 입장하기
    @PostMapping("/habits/{habit_id}/habit-sessions")
    public HabitSessionEnterPostResponseDto enterHabitSession(@PathVariable Long habit_id, User user){
        return habitSessionService.enterHabitSession(habit_id, user);
    }

    // 세션 정보 조회
    @GetMapping("/habit-sessions/{session_id}")
    public HabitSessionGetResponseDto getHabitSession(@PathVariable Long session_id, User user){
        return habitSessionService.getHabitSession(session_id,user);
    }

    // 습관 인증샷 올리기
    @PostMapping("/habit-sessions/{session_id}/upload/{user_id}")
    public HabitSessionUploadVideoPostResponseDto uploadVideo(@PathVariable Long session_id, @RequestParam(value="image") MultipartFile file, @PathVariable Long user_id) throws IOException {
        //if (file.isEmpty()){} // TODO: 에러 처리
        User user = userService.findById(user_id);
        return habitSessionService.uploadVideo(session_id, file, user);
    }

    // 베스트 습관러 후보 조회
    @GetMapping("/habit-sessions/{session_id}/vote")
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(@PathVariable Long session_id){
        return habitSessionService.getMvpCandidateList(session_id);
    }

    // 베스트 습관러 투표하기
    @PostMapping("/habit-sessions/{session_id}/vote")
    public HabitSessionMvpVotePostResponseDto voteMvp(@PathVariable Long session_id,User user,@RequestBody HabitSessionMvpVotePostRequestDto requestDto){
        return habitSessionService.voteMvp(session_id, user, requestDto);
    }

    // 베스트 습관러 결과 보여주기
    @GetMapping("/habit-sessions/{session_id}/mvp")
    public List<HabitSessionMvpGetResponseDto> showMvp(@PathVariable Long session_id, User user){
        return habitSessionService.showMvp(session_id, user);
    }

    // 습관 회고 쓰기
    @PostMapping("/habit-sessions/{session_id}/review")
    public HabitSessionReviewPostResponseDto writeReview(@PathVariable Long session_id, @RequestBody HabitSessionReviewPostRequestDto requestDto, User user)
    {return habitSessionService.writeReview(session_id,requestDto,user);
    }

}
