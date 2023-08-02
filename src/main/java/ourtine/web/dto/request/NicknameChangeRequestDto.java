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
public class NicknameChangeRequestDto {

    @NotBlank
    @Length(min = 2, max = 10, message = "2~10 글자 닉네임만 가능합니다.")
    @Pattern(regexp="^[ㄱ-ㅎ가-힣a-z0-9-]*")
    private String nickname;

}
