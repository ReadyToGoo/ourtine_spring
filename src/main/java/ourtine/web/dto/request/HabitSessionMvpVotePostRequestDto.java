package ourtine.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Positive(message = "자연수만 입력가능합니다.")
    @Schema(title = "투표할 유저의 아이디", description = "mvp 후보에 있는 유저에게만 투표가 가능")
    private Long mvpVote;
}
