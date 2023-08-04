package ourtine.web.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class FollowGetRequestDto {
    @NotBlank(message = "유저 입력은 필수입니다.")
    Long userId;
}
