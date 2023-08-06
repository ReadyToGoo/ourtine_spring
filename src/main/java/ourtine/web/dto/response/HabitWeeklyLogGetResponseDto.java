package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.Emotion;


@Getter
@AllArgsConstructor
public class HabitWeeklyLogGetResponseDto {
    private Day day;
    private String videoUrl;
    private Emotion emotion;
}
