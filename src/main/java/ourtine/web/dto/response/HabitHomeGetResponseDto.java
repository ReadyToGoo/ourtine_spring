package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitHomeGetResponseDto {
    List<HabitProfileHomeGetResponseDto> today;
    List<HabitProfileHomeGetResponseDto> others;
}
