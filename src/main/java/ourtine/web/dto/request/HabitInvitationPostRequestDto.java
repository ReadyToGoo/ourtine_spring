package ourtine.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HabitInvitationPostRequestDto {

    @NotBlank
    List<Long> friends = new ArrayList<>();
}
