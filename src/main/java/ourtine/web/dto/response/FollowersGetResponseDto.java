package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowersGetResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
