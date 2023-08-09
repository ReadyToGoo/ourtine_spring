package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowGetResponseDto {
    private Long userId;
    private Boolean isFollow;
}
