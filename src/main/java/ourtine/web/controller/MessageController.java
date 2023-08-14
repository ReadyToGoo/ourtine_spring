package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;
import ourtine.service.MessageService;
import ourtine.service.UserService;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.web.dto.response.MessageResponseDto;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("message")
    @ApiOperation(value = "유저 메시지 조회", notes = "유저에게 온 메시지를 조회한다.")
    public BaseResponseDto<SliceResponseDto<MessageResponseDto>> getMessages(Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        Long userId = user.getId();
        Slice<MessageResponseDto> messages = messageService.getMessages(user, pageable);
        return new BaseResponseDto<>(new SliceResponseDto<>(messages));
    }
}
