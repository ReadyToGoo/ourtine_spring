package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.common.SliceResponseDto;

@Getter
@AllArgsConstructor
public class HabitUserFollowingListGetResponseDto {

    private Boolean isFriend; // 유저가 친구인지 아닌지 보여주는 필드

    private SliceResponseDto<HabitFollowingInfoDto> habits; // 친구가 아닌 유저

    private SliceResponseDto<HabitFollowingInfoDto> commonHabits ; // 친구인 유저

    private SliceResponseDto<HabitFollowingInfoDto> otherHabits ; // 친구인 유저



}
