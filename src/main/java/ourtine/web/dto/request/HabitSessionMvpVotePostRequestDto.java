package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSessionMvpVotePostRequestDto {
    @NotBlank(message = "유저 입력은 필수 입니다.")
    private Long mvpVote;
}
