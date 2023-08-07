package ourtine.web.dto.request;

import lombok.*;
import ourtine.domain.enums.Emotion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitSessionReviewPostRequestDto {

    @NotNull(message = "만족도 입력은 필수입니다.")
    Long starRate;
    @NotNull(message = "감정 입력은 필수입니다")
    Emotion emotion;
}
