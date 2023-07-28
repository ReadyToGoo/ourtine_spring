package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitSessionUploadVideoPostResponseDto {
    private Long sessionId;
    private Long userId;
}
