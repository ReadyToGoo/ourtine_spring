package ourtine.web.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowPostRequestDto {
    @NotNull(message = "유저 입력은 필수입니다.")
    private Long userId;
}
