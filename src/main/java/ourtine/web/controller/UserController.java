package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.UploadService;
import ourtine.domain.Category;
import ourtine.domain.User;
import ourtine.domain.enums.CategoryList;
import ourtine.service.CategoryService;
import ourtine.service.FollowService;
import ourtine.service.UserCategoryService;
import ourtine.validator.NicknameValidator;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.request.FollowGetRequestDto;
import ourtine.web.dto.request.GoalChangeRequestDto;
import ourtine.web.dto.request.NicknameChangeRequestDto;
import ourtine.service.UserService;
import ourtine.web.dto.response.UserAlertResponseDto;
import ourtine.web.dto.response.UserProfileDto;
import ourtine.web.dto.response.UserUpdateResponseDto;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UploadService uploadService;
    private final UserCategoryService userCategoryService;
    private final CategoryService categoryService;
    private final FollowService followService;
    private final NicknameValidator nicknameValidator;
    @InitBinder("targetObject")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(nicknameValidator);
        //binder.setValidator(new NicknameValidator());
    }

    // 프로필 사진 업로드 추가를 위해 임시로 만든 회원가입 API
    @PostMapping("/user/signup")
    @ApiOperation(value = "(미완)회원 가입",notes="(미완)회원 가입을 한다.")
    public ResponseEntity signUp(@RequestParam(value="image") MultipartFile image) throws IOException {
        User user = new User();
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @GetMapping("user/{userId}/myPage")
//    public BaseResponseDto<UserProfileDto> myPage(@PathVariable Long userId) {
//
//
//    }

    @GetMapping("user/{userId}/profile/{myId}")
    public BaseResponseDto<UserProfileDto> getUserProfile(@PathVariable Long userId, @PathVariable Long myId) {
        User user = userService.findById(myId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = userCategoryService.findUsersAllCategory(user.getId());
        Boolean isFollow = followService.getFollowStatus(userId, myId).getIsFollow();
        Long followerCount = followService.getFollowerCount(userId, user, pageable);
        Long followingCount = followService.getFollowingCount(userId, user, pageable);
        UserProfileDto userProfileDto = new UserProfileDto(user,categories, isFollow, followerCount, followingCount);
        return new BaseResponseDto<>(userProfileDto);
    }


    @PatchMapping("/user/{userId}/nickname")
    @ApiOperation(value = "닉네임 변경",notes="User의 닉네임을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeNickname(@PathVariable Long userId, @RequestBody @Valid NicknameChangeRequestDto nicknameChangeRequestDto){//형식에 맞게 수정 필요
        userService.changeNickname(userId, nicknameChangeRequestDto.getNickname());
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }

    @PatchMapping("/user/{userId}/goal")
    @ApiOperation(value = "다짐 변경",notes="User의 다짐을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeGoal(@PathVariable Long userId, @RequestBody @Valid GoalChangeRequestDto goalChangeRequestDto) {
        userService.changeGoal(userId, goalChangeRequestDto.getGoal());
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }

    @PatchMapping("user/{userId}/category")
    @ApiOperation(value = "관심 카테고리 변경",notes="User의 관심 카테고리 목록을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeCategory(@PathVariable Long userId, @RequestBody List<CategoryList> categoryLists) {
        User user = userService.findById(userId);
        userCategoryService.deleteUsersAllCategory(userId);

        List<Category> categories = categoryService.findCategories(categoryLists);
        userCategoryService.saveCategories(user, categories);
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }

    @PatchMapping(value = "/user/{userId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "유저 프로필 사진 변경", notes = "유저의 프로필 사진을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeUserProfileImage(@PathVariable Long userId, @RequestBody MultipartFile image) throws IOException {
        User user = userService.findById(userId);
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }

    @GetMapping(value = "/user/{userId}/alerts")
    @ApiOperation(value = "유저의 푸쉬 알림 설정들 조회", notes = "유저의 푸쉬 알림, 마케팅 푸쉬 알림 설정 값을 조회한다.")
    public BaseResponseDto<UserAlertResponseDto> getUserPushAlerts(@PathVariable Long userId) {
        User user = userService.findById(userId);
        UserAlertResponseDto userAlertResponseDto = new UserAlertResponseDto(user.isPushAlert(), user.isMarketingPushAlert());
        return new BaseResponseDto<>(userAlertResponseDto);
    }

    @PatchMapping(value= "/user/{userId}/pushAlert")
    @ApiOperation(value = "유저 푸쉬 알림 변경", notes = "유저의 푸쉬 알림 설정을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeUserPushAlert(@PathVariable Long userId) {
        userService.changePushAlert(userId);
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }

    @PatchMapping(value= "/user/{userId}/marketingPushAlert")
    @ApiOperation(value = "유저 마케팅 푸쉬 알림 변경", notes = "유저의 마케팅 푸쉬 알림 설정을 변경한다.")
    public BaseResponseDto<UserUpdateResponseDto> changeUserMarketingPushAlert(@PathVariable Long userId) {
        userService.changeMarketingPushAlert(userId);
        return new BaseResponseDto<>(new UserUpdateResponseDto(userId));
    }
}
