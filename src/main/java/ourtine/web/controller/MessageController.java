package ourtine.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ourtine.domain.User;
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

    @GetMapping("message/{userId}")
    public BaseResponseDto<SliceResponseDto<MessageResponseDto>> getMessages(@PathVariable Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        Slice<MessageResponseDto> messages = messageService.getMessages(user, pageable);

        return new BaseResponseDto<>(new SliceResponseDto<>(messages));
    }
}
