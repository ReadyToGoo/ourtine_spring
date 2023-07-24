package ourtine.server.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitVoteMvpRequest {
    @NotBlank(message = "투표할 유저를 입력해주세요.")
    private Long mvpVote;
}
