package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitInvitationPostRequestDto {

    @NotNull(message = "습관 아이디 입력은 필수입니다.")
    @Positive(message = "양수만 입력가능합니다.")
    private Long habitId ;

    @NotEmpty(message = "유저 입력은 필수입니다.")
    private List<Long> friends ;
}
