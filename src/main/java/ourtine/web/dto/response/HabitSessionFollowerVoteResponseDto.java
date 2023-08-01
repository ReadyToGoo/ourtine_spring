package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.HabitFollowerStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionFollowerVoteResponseDto {
    private Long id;

    private String nickname;

    private String profileImg;

    private String videoUrl;

    private HabitFollowerStatus habitFollowerStatus; // 습관 완료 여부

}
