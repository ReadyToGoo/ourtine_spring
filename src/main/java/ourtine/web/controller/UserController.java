package ourtine.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.UploadService;
import ourtine.domain.User;
import ourtine.web.dto.request.NicknameChangeRequestDto;
import ourtine.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UploadService uploadService;

    @PostMapping("/user/{id}/nickname")
    public ResponseEntity ChangeNickname(@PathVariable Long id, @RequestBody @Valid NicknameChangeRequestDto nicknameChangeRequestDto){//형식에 맞게 수정 필요
        userService.changeNickname(id, nicknameChangeRequestDto.getNickname());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
    @PatchMapping(value="/user/{id}/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadUserProfile(@PathVariable Long id,  @RequestParam(value="image") MultipartFile image) throws IOException {
        User user = userService.findById(id);
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);
    }
}
