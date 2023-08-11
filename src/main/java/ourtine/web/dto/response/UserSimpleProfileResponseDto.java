package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ourtine.domain.User;

@Getter
@AllArgsConstructor
@Builder
public class UserSimpleProfileResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;

    public UserSimpleProfileResponseDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getImageUrl();
    }

}
