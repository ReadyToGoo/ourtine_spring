package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionMvpCandidateGetResponseDto {
    private Long sessionId;
    private List<HabitSessionFollowerVoteResponseDto> candidates;
}
