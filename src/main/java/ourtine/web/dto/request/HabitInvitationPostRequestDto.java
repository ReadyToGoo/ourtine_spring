package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HabitInvitationPostRequestDto {

    @NotBlank (message = "습관 아이디 입력은 필수입니다.")
    Long habitId ;

    @NotBlank (message = "유저 입력은 필수입니다.")
    List<Long> friends = new ArrayList<>();
}
