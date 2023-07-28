package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.CompleteStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionFollowerGetResponseDto {
    private Long id;

    private String nickname;

    private String profileImg;

    private String videoUrl;

    private CompleteStatus completeStatus ; // 습관 완료 여부

}
