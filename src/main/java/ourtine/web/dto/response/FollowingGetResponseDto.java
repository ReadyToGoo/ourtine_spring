package ourtine.web.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowingGetResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
