package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitFollowersGetResponseDto {
    private Long id;

    private String nickname;

    private String imageUrl;

    private boolean isHost;
}
