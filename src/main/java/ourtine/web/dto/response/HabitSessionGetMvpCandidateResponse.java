package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionGetMvpCandidateResponse {
    private Long id;

    private String nickname;

    private String profileImg;

    private String videoUrl;

    private boolean enterStatus; // 입장 여부

    private boolean completeStatus ; // 습관 완료 여부

}
