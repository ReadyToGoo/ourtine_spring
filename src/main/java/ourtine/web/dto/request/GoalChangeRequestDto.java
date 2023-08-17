package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalChangeRequestDto {

    @NotBlank(message = "10~50 글자 사이로 입력해주세요.")
    @Length(min = 10, max = 50, message = "10~50 글자 사이로 입력해주세요.")
    private String goal;

}
