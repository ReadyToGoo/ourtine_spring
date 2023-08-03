package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitSessionFollowerResponseDto {
    Long userId;
    String nickname;
    String imageUrl;
}
