package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class FollowDeleteRequestDto {
    @NotNull(message = "유저 입력은 필수입니다.")
    Long userId;
}
