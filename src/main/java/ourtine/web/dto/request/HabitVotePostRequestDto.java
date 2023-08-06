package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitVotePostRequestDto {
    @NotNull(message = "유저 입력은 필수 입니다.")
    private Long mvpVote;
}
