package ourtine.web.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.*;
import ourtine.domain.enums.Emotion;

import javax.validation.constraints.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class HabitSessionReviewPostRequestDto {

    @NotNull(message = "만족도 입력은 필수입니다.")
    @Positive(message = "양수만 입력가능합니다.")
    @Min(value = 1, message = "1 이상 5 이하의 숫자만 입력 가능합니다.")
    @Max(value = 5, message = "1 이상 5 이하의 숫자만 입력 가능합니다.")
    private Long starRate;

    @NotNull(message = "감정 입력은 필수입니다")
    private Emotion emotion;
}
