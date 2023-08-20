package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.Emotion;

import java.util.Date;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitWeeklyLogGetResponseDto {
    private Day day;
    private Date date;
    private String title;
    private String videoUrl;
    private Emotion emotion;
}
