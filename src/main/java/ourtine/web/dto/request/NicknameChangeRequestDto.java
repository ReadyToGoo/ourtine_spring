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
    @Length(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    @Pattern(regexp="^[ㄱ-ㅎ가-힣a-z0-9-]*", message = "닉네임은 공백과 특수문자를 불포함한 글자만 입력할 수 있습니다.")
    private String nickname;

}
