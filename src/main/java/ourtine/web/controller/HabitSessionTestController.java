package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.repository.UserRepository;
import ourtine.service.impl.HabitSessionServiceImpl;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class HabitSessionTestController {
    private final HabitSessionServiceImpl habitSessionService;
    private final UserRepository userRepository;

    // 세션 입장하기
    @PostMapping("/{my_id}/habits/{habit_id}/habit-sessions")
    @ApiOperation(value = "홈 - 습관 세션 입장", notes = "습관 세션에 입장한다.")
    public HabitSessionEnterPostResponseDto enterHabitSession(@PathVariable Long habit_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.enterHabitSession(habit_id, user);
    }

    // 세션 정보 조회
    @GetMapping("/{my_id}/habit-sessions/{session_id}")
    @ApiOperation(value = "홈 - 습관 세션 ", notes = "입장한 습관 세션의 정보를 조회한다.")
    public HabitSessionGetResponseDto getHabitSession(@PathVariable Long session_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.getHabitSession(session_id,user);
    }

    // 습관 인증샷 올리기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/upload")
    @ApiOperation(value = "습관 세션 - 습관 인증", notes = "진행한 습관 인증 영상을 올린다.")
    public HabitSessionUploadVideoPostResponseDto uploadVideo(@PathVariable Long session_id, @RequestPart MultipartFile file, @PathVariable Long my_id) throws IOException {
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.uploadVideo(session_id, file, user);
    }

    // 베스트 습관러 후보 조회
    @GetMapping("/{my_id}/habit-sessions/{session_id}/vote")
    @ApiOperation(value = "습관 세션 - 베스트 습관러 투표", notes = "진행한 습관에 대한 베스트 습관러 후보를 조회한다.")
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(@PathVariable Long session_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.getMvpCandidateList(session_id);
    }

    // 베스트 습관러 투표하기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/vote")
    @ApiOperation(value = "습관 세션 - 베스트 습관러 투표", notes = "진행한 습관에 대한 베스트 습관러를 투표한다.")
    public HabitSessionMvpVotePostResponseDto voteMvp(@PathVariable Long session_id,@PathVariable Long my_id,@RequestBody @Valid HabitSessionMvpVotePostRequestDto requestDto){
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.voteMvp(session_id, user, requestDto);
    }

    // 베스트 습관러 결과 보여주기
    @GetMapping("/{my_id}/habit-sessions/{session_id}/mvp")
    @ApiOperation(value = "습관 세션 - 베스트 습관러",notes = "진행한 습관에 대한 베스트 습관러를 조회한다.")
    public List<HabitSessionMvpGetResponseDto> showMvp(@PathVariable Long session_id,@PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.showMvp(session_id, user);
    }

    // 습관 회고 쓰기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/review")
    @ApiOperation(value = "습관 세션 - 회고",notes = "진행한 습관에 대한 회고를 기록한다.")
    public HabitSessionReviewPostResponseDto writeReview(@PathVariable Long session_id, @RequestBody @Valid HabitSessionReviewPostRequestDto requestDto, @PathVariable Long my_id)
    {
        User user = userRepository.findById(my_id).orElseThrow();
        return habitSessionService.writeReview(session_id,requestDto,user);
    }


}
