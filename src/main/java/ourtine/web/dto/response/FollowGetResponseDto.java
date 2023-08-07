package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FollowGetResponseDto {
    Long userId;
    Boolean isFollow;
}
