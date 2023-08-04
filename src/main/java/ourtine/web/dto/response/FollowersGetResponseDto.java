package ourtine.web.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowersGetResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
