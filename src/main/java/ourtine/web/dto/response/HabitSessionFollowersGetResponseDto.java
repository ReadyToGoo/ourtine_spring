package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionFollowersGetResponseDto {

        private Long id;

        private String nickname;

        private String imageUrl;

        private boolean enterStatus;
}
