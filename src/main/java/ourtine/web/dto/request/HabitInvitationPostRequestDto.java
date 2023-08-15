package ourtine.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitInvitationPostRequestDto {

    @NotNull(message = "습관 아이디 입력은 필수입니다.")
    @Positive(message = "양수만 입력가능합니다.")
    @ApiModelProperty(value = "습관 아이디")
    private Long habitId ;

    @NotEmpty(message = "유저 입력은 필수입니다.")
    @ApiModelProperty(value = "초대할 유저의 아이디 리스트", notes = "습관의 인원수-1 명의 인원만 초대 가능합니다.")
    private List<Long> users;
}
