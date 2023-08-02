
package ourtine.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitFollowersGetResponseDto {
    private Long id;

    private Boolean isHost;

    private String nickname;

    private String imageUrl;

}
