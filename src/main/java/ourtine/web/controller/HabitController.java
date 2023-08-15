package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.UploadService;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.enums.Sort;
import ourtine.service.MessageService;
import ourtine.service.UserService;
import ourtine.service.impl.HabitServiceImpl;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import ourtine.web.dto.request.HabitInvitationPostRequestDto;
import ourtine.web.dto.response.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitServiceImpl habitService;
    private final UploadService uploadService;
    private final MessageService messageService;

    // 습관 개설
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "습관 개설 - 습관 개설하기", notes = "습관을 개설한다.")
    public BaseResponseDto<HabitCreatePostResponseDto> createHabit(@RequestPart @Valid HabitCreatePostRequestDto habitCreatePostRequestDto,
                                                  @RequestPart MultipartFile file) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.createHabit(habitCreatePostRequestDto,file,user));
    }

    // 습관 프로필 사진 수정
    @PatchMapping(value="/{habit_id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "습관 프로필 - 사진 변경",notes="습관의 프로필 사진을 변경한다.")
    public BaseResponseDto<HabitUpdateImagePatchResponseDto> updateHabitProfileImage(@PathVariable Long habit_id, @RequestParam(value="image") MultipartFile image) throws IOException {
        Habit habit = habitService.findById(habit_id);
        habit.updateImage(uploadService.uploadHabitProfile(image));
        habitService.saveOrUpdateHabit(habit);
        return new BaseResponseDto<>(new HabitUpdateImagePatchResponseDto(habit_id));
    }

    // 홈 - 팔로잉하는 습관 목록
    @GetMapping("/me")
    @ApiOperation(value = "홈 - 습관 참여 리스트", notes = "오늘 내가 진행할 습관들을 조회한다.")
    public BaseResponseDto<SliceResponseDto<HabitMyFollowingListGetResponseDto>> getMyTodaysMyHabits(Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(habitService.getTodaysMyHabits(user,pageable)));
    }

    // 습관 프로필 조회
    @GetMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 프로필", notes = "특정 습관 하나를 조회한다.")
    public BaseResponseDto< HabitGetResponseDto> getHabit(@PathVariable Long habit_id){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.getHabit(habit_id,user));
    }

    // 습관 프로필 - 습관 위클리 로그
    @GetMapping(value = "/{habit_id}/weekly-log")
    @ApiOperation(value = "습관 프로필 - 위클리 로그", notes = "내가 참여하는 특정 습관에 대한 위클리로그를 조회한다.")
    public BaseResponseDto<SliceResponseDto<HabitDailyLogGetResponseDto>> getHabitWeeklyLog(@PathVariable Long habit_id){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(habitService.getHabitWeeklyLog(habit_id,user)));
    }

    // 내 프로필 - 위클리 로그
    @GetMapping(value = "/me/weekly-log")
    @ApiOperation(value = "마이 페이지 - 위클리 로그", notes = "이번주에 내가 진행했던 습관 기록들에 대해 조회한다.")
    public BaseResponseDto<List<HabitWeeklyLogGetResponseDto>> getMyWeeklyLog(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.getMyWeeklyLog(user));
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @GetMapping(value = "/following/users/{user_id}")
    @ApiOperation(value = "유저 프로필", notes = "특정 유저의 참여중인 습관 목록을 조회한다.")
    public BaseResponseDto<HabitUserFollowingListGetResponseDto> getUserFollowingHabits(@PathVariable Long user_id, Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User me = userDetails.getUser();
        return new BaseResponseDto<>(habitService.getUserFollowingHabits(user_id, me, pageable));
    }

    // 유저 프로필 - 참여 했던 습관 목록
    @GetMapping(value = "/followed/users/{user_id}")
    @ApiOperation(value = "유저 프로필", notes = "특정 유저의 참여했던 습관 목록을 조회한다.")
    public BaseResponseDto<SliceResponseDto<HabitUserFollowedGetResponseDto>> getUserFollowedHabits(@PathVariable Long user_id, Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User me = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(habitService.getUserFollowedHabits(user_id, me, pageable)));
    }

    // 추천 습관 목록
    @GetMapping(value = "/recommend")
    @ApiOperation(value = "참여 - 추천 습관", notes = "유저가 관심있는 카테고리에 대한 습관을 조회한다.")
    public BaseResponseDto<SliceResponseDto<HabitRecommendResponseDto>> getRecommendHabits(Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User me = userDetails.getUser();
        return new BaseResponseDto<>(new  SliceResponseDto<>(habitService.getRecommendHabits(me, pageable)));
    }

    // 습관 참여하기
    @PostMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 프로필 -  참여", notes = "습관에 참여 신청을 한다.")
    public BaseResponseDto<HabitFollowerResponseDto> joinHabit(@PathVariable @Valid Long habit_id){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.joinHabit(habit_id,user));
    }

    // 습관 검색하기
    @GetMapping(value = "/search")
    @ApiOperation(value = "참여 - 검색", notes = "원하는 정렬 필터와 키워드로 습관을 검색한다.")
    public BaseResponseDto<SliceResponseDto<HabitSearchResponseDto>> searchHabits(@RequestParam Sort sort_by,  @RequestParam String keyword, Pageable pageable){
        String word = '%'+keyword+'%';
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(habitService.searchHabits(sort_by,user,word,pageable)));
    }

    // 카테고리별 검색
    @GetMapping("/discover")
    @ApiOperation(value = "참여 - 카테고리별 습관 추천", notes = "카테고리로 분류된 습관들을 조회한다.")
    public BaseResponseDto<SliceResponseDto<HabitFindByCategoryGetResponseDto>> findHabitsByCategory(@RequestParam CategoryList category, Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(habitService.findHabitsByCategory(category, user, pageable)));
    }

    // 습관 참여 취소하기
    @DeleteMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 프로필 - 습관 참여 취소", notes = "참여하고 있는 습관을 취소한다.")
    public BaseResponseDto<HabitFollowerResponseDto> quitHabit(@PathVariable Long habit_id){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.quitHabit(habit_id, user));
    }

    // 습관 삭제
    @DeleteMapping(value = "/{habit_id}/delete")
    @ApiOperation(value = "습관 프로필 - 습관 삭제 ", notes = "습관을 삭제한다.")
    public BaseResponseDto<HabitDeleteResponseDto> deleteHabit(@PathVariable Long habit_id){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(habitService.deleteHabit(habit_id, user));
    }

    // 습관 초대장
    @PostMapping("/invite")
    @ApiOperation(value = "습관 개설 - 습관 초대", notes = "유저들에게 습관 초대장을 보낸다.")
    public BaseResponseDto<HabitInvitationPostResponseDto> sendInvitation (@RequestBody @Valid HabitInvitationPostRequestDto requestDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        Habit habit = habitService.findById(requestDto.getHabitId());
        return new BaseResponseDto<>( messageService.newHabitInviteMessage(user, requestDto.getFriends(), habit));
    }

}

