package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.enums.Emotion;

import java.util.Date;

@Getter
@AllArgsConstructor
public class HabitWeeklyLogResponseDto {
    Date date;
    Emotion emotion;
}
