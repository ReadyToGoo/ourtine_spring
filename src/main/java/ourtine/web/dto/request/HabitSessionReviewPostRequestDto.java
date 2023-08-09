package ourtine.web.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.*;
import ourtine.domain.enums.Emotion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class HabitSessionReviewPostRequestDto {

    @NotNull(message = "만족도 입력은 필수입니다.")
    private Long starRate;
    @NotNull(message = "감정 입력은 필수입니다")
    private Emotion emotion;
}
