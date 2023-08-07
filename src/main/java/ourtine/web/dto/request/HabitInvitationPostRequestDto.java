package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
public class HabitInvitationPostRequestDto {

    @NotNull(message = "습관 아이디 입력은 필수입니다.")
    Long habitId ;

    @NotEmpty(message = "유저 입력은 필수입니다.")
    List<Long> friends ;
}
