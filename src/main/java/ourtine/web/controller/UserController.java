package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import ourtine.service.UserCategoryService;
import ourtine.validator.NicknameValidator;
import ourtine.web.dto.request.GoalChangeRequestDto;
import ourtine.web.dto.request.NicknameChangeRequestDto;
import ourtine.service.UserService;

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

    @PatchMapping("/user/{userId}/nickname")
    @ApiOperation(value = "닉네임 변경",notes="User의 닉네임을 변경한다.")
    public ResponseEntity changeNickname(@PathVariable Long userId, @RequestBody @Valid NicknameChangeRequestDto nicknameChangeRequestDto){//형식에 맞게 수정 필요
        userService.changeNickname(userId, nicknameChangeRequestDto.getNickname());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/user/{userId}/goal")
    @ApiOperation(value = "다짐 변경",notes="User의 다짐을 변경한다.")
    public ResponseEntity changeGoal(@PathVariable Long userId, @RequestBody @Valid GoalChangeRequestDto goalChangeRequestDto) {
        userService.changeGoal(userId, goalChangeRequestDto.getGoal());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("user/{userId}/category")
    @ApiOperation(value = "관심 카테고리 변경",notes="User의 관심 카테고리 목록을 변경한다.")
    public ResponseEntity changeCategory(@PathVariable Long userId, @RequestBody List<CategoryList> categoryLists) {
        User user = userService.findById(userId);
        userCategoryService.deleteUsersAllCategory(userId);

        List<Category> categories = categoryService.findCategories(categoryLists);
        userCategoryService.saveCategories(user, categories);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping(value = "/user/{userId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "유저 프로필 사진 변경", notes = "유저의 프로필 사진을 변경한다.")
    public ResponseEntity changeUserProfileImage(@PathVariable Long userId, @RequestBody MultipartFile image) throws IOException {
        User user = userService.findById(userId);
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    //@GetMapping(value="/user/{userId}/alerts")


    @PatchMapping(value= "/user/{userId}/pushAlert")
    @ApiOperation(value = "유저 푸쉬 알림 변경", notes = "유저의 푸쉬 알림 설정을 변경한다.")
    public ResponseEntity changeUserPushAlert(@PathVariable Long userId) {
        userService.changePushAlert(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping(value= "/user/{userId}/marketingPushAlert")
    @ApiOperation(value = "유저 마케팅 푸쉬 알림 변경", notes = "유저의 마케팅 푸쉬 알림 설정을 변경한다.")
    public ResponseEntity changeUserMarketingPushAlert(@PathVariable Long userId) {
        userService.changeMarketingPushAlert(userId);
        return new ResponseEntity(HttpStatus.OK);

    }
}
