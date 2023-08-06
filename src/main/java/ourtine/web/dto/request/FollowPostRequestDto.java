package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class FollowPostRequestDto {
    @NotBlank(message = "유저 입력은 필수입니다.")
    Long userId;
}
