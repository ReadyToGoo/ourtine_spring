package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
public class HabitSessionFollowersGetResponseDto {

        private Long entryNum;

        private Long userId;

        private String nickname;

        private String imageUrl;
}
