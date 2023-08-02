package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitSessionMvpGetResponseDto {
    private Long userId;
    private String nickname;
    private String imageUrl;
}
