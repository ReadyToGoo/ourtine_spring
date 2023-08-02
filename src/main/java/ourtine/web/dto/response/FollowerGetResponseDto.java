package ourtine.web.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowerGetResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
