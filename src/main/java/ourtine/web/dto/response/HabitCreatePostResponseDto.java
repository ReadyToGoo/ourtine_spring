package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitCreatePostResponseDto {
    private Long habitId;
    private Long habitNum; // 유저가 몇번째로 개설한 습관인지
}
