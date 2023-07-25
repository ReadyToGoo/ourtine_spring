package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitActiveSessionGetResponse {
    private Long id;

    private String title;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<HabitSessionFollowersGetResponseDto> followers = new ArrayList<>();
}
