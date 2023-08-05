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
import ourtine.domain.User;
import ourtine.validator.NicknameValidator;
import ourtine.web.dto.request.NicknameChangeRequestDto;
import ourtine.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UploadService uploadService;
    private final NicknameValidator nicknameValidator;
    @InitBinder
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

    @PatchMapping("/user/{id}/nickname")
    @ApiOperation(value = "닉네임 변경",notes="User의 닉네임을 변경한다.")
    public ResponseEntity changeNickname(@PathVariable Long id, @RequestBody @Valid NicknameChangeRequestDto nicknameChangeRequestDto){//형식에 맞게 수정 필요
        userService.changeNickname(id, nicknameChangeRequestDto.getNickname());
        return new ResponseEntity(HttpStatus.OK);
    }

//    @PatchMapping("/user/{id}/goal")
//    public ResponseEntity changeGoal(@PathVariable Long id,)

    @PatchMapping(value = "/user/{id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "유저 프로필 사진 변경", notes = "유저의 프로필 사진을 변경한다.")
    public ResponseEntity changeUserProfileImage(@PathVariable Long id, @RequestParam(value = "image") MultipartFile image) throws IOException {
        User user = userService.findById(id);
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }
}
