package ourtine.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;
import ourtine.repository.UserRepository;
import ourtine.service.impl.HabitSessionServiceImpl;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Slf4j
public class HabitSessionTestController {
    private final HabitSessionServiceImpl habitSessionService;
    private final UserRepository userRepository;

    // 세션 아이디 조회
    @GetMapping("/{my_id}/habits/{habit_id}/habit-sessions")
    @Operation(summary = "세션 ID 조회", description = "세션 ID를 조회한다.")
    public BaseResponseDto<HabitSessionIdGetResponseDto> getSessionId(@PathVariable Long habit_id,  @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.getSessionId(habit_id, user));
    }

    // 세션 입장하기
    @PostMapping("/{my_id}/habits/{habit_id}/habit-sessions")
    @Operation(summary = "홈 - 습관 세션 입장", description = "습관 세션에 입장한다.")
    public BaseResponseDto<HabitSessionEnterPostResponseDto> enterHabitSession(@PathVariable Long habit_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.enterHabitSession(habit_id, user));
    }

    // 세션 정보 조회
    @GetMapping("/{my_id}/habit-sessions/{session_id}")
    @Operation(summary = "홈 - 습관 세션 ", description = "입장한 습관 세션의 정보를 조회한다.")
    public BaseResponseDto<HabitSessionGetResponseDto> getHabitSession(@PathVariable Long session_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.getHabitSession(session_id,user));
    }

    // 습관 인증샷 올리기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/upload")
    @Operation(summary = "습관 세션 - 습관 인증", description = "진행한 습관 인증 영상을 올린다.")
    public BaseResponseDto<HabitSessionUploadVideoPostResponseDto> uploadVideo(@PathVariable Long session_id, @RequestPart MultipartFile file, @PathVariable Long my_id) throws IOException {
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.uploadVideo(session_id, file, user));
    }

    // 베스트 습관러 후보 조회
    @GetMapping("/{my_id}/habit-sessions/{session_id}/vote")
    @Operation(summary = "습관 세션 - 베스트 습관러 투표", description = "진행한 습관에 대한 베스트 습관러 후보를 조회한다.")
    public BaseResponseDto<HabitSessionMvpCandidateGetResponseDto> getMvpCandidateList(@PathVariable Long session_id, @PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.getMvpCandidateList(session_id));
    }

    // 베스트 습관러 투표하기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/vote")
    @Operation(summary = "습관 세션 - 베스트 습관러 투표", description = "진행한 습관에 대한 베스트 습관러를 투표한다.")
    public BaseResponseDto<HabitSessionMvpVotePostResponseDto> voteMvp(@PathVariable Long session_id,@PathVariable Long my_id,@RequestBody @Valid HabitSessionMvpVotePostRequestDto requestDto){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.voteMvp(session_id, user, requestDto));
    }

    // 베스트 습관러 결과 보여주기
    @GetMapping("/{my_id}/habit-sessions/{session_id}/mvp")
    @Operation(summary = "습관 세션 - 베스트 습관러",description = "진행한 습관에 대한 베스트 습관러를 조회한다.")
    public BaseResponseDto<List<HabitSessionMvpGetResponseDto>> showMvp(@PathVariable Long session_id,@PathVariable Long my_id){
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.showMvp(session_id, user));
    }

    // 습관 회고 쓰기
    @PatchMapping("/{my_id}/habit-sessions/{session_id}/review")
    @Operation(summary = "습관 세션 - 회고",description = "진행한 습관에 대한 회고를 기록한다.")
    public BaseResponseDto<HabitSessionReviewPostResponseDto> writeReview(@PathVariable Long session_id, @RequestBody @Valid HabitSessionReviewPostRequestDto requestDto, @PathVariable Long my_id)
    {
        User user = userRepository.findById(my_id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_USER));
        return new BaseResponseDto<>(habitSessionService.writeReview(session_id,requestDto,user));
    }


}
