package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.web.dto.common.SliceResponseDto;

@Getter
@AllArgsConstructor
public class HomeResponseDto {
    private String userWeeklyLogPeriod;
    private String userWeeklyLogContents;

    private SliceResponseDto<HabitMyFollowingListGetResponseDto> habits;

//    public HomeResponseDto(String userWeeklyLogPeriod, String userWeeklyLogContents,
//                           SliceResponseDto<HabitMyFollowingListGetResponseDto> habits) {
//        this.userWeeklyLogPeriod=userWeeklyLogPeriod;
//        this.userWeeklyLogContents = userWeeklyLogContents;
//        this.habits = habits;
//    }

}
