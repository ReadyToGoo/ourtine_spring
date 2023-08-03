package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.enums.Emotion;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class HabitSessionReviewPostRequestDto {
    @NotBlank
    Long starRate;
    @NotBlank
    Emotion emotion;
}
