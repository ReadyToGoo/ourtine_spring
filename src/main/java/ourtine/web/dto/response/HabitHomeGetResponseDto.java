package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class HabitHomeGetResponseDto {
    private String userWeeklyLogPeriod;
    private String userWeeklyLogContents;
    List<HabitProfileHomeGetResponseDto> today;
    List<HabitProfileHomeGetResponseDto> others;

    public HabitHomeGetResponseDto(List<HabitProfileHomeGetResponseDto> today, List<HabitProfileHomeGetResponseDto> others) {
        this.userWeeklyLogPeriod="";
        this.userWeeklyLogContents="";
        this.today=today;
        this.others = others;
    }

}
