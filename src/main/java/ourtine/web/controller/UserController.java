package ourtine.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ourtine.web.dto.request.NicknameChangeRequestDto;
import ourtine.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/{id}/nickname")
    public ResponseEntity ChangeNickname(@PathVariable Long id, @RequestBody @Valid NicknameChangeRequestDto nicknameChangeRequestDto){//형식에 맞게 수정 필요
        userService.changeNickname(id, nicknameChangeRequestDto.getNickname());
        return new ResponseEntity(HttpStatus.OK);
    }
}
