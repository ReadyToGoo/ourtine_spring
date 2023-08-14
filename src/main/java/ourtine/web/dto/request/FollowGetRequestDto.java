package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowGetRequestDto {
    @NotNull(message = "유저 입력은 필수입니다.")
    private Long userId;
}
