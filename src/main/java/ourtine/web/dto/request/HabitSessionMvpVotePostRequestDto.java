package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitSessionMvpVotePostRequestDto {
    @NotNull(message = "유저 입력은 필수 입니다.")
    @Positive(message = "양수만 입력가능합니다.")
    private Long mvpVote;
}
