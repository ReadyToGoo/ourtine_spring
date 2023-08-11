package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.HabitFollowerStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSessionFollowerResponseDto {
    private Long userId;
    private String nickname;
    private String imageUrl;
    private HabitFollowerStatus habitFollowerStatus; // 습관 완료 여부
}
