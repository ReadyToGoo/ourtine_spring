package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ourtine.domain.enums.Emotion;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class HabitSessionReviewPostRequestDto {
    @NotBlank(message = "만족도 입력은 필수입니다.")
    Long starRate;
    @NotBlank(message = "감정 입력은 필수입니다")
    Emotion emotion;
}
