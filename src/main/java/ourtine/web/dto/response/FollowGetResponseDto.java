package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowGetResponseDto {
    Long userId;
    Boolean isFollow;
}
