package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ourtine.domain.User;
import ourtine.service.MessageService;
import ourtine.service.UserService;
import ourtine.service.impl.FollowServiceImpl;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow/test")
public class FollowTestController {
    private final FollowServiceImpl followService;
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/{my_id}")
    @ApiOperation(value = "유저 팔로우", notes = "특정 유저를 팔로우 한다.")
    public BaseResponseDto<FollowPostResponseDto> followUser(@RequestBody FollowPostRequestDto requestDto, @PathVariable Long my_id){
        User user = userService.findById(my_id);
        FollowPostResponseDto followPostResponseDto = followService.followUser(requestDto, user);
        User receiver = userService.findById(requestDto.getUserId());
        messageService.newFollowMessage(user, receiver);
        return new BaseResponseDto<>(followPostResponseDto);
    }
    @DeleteMapping("/{my_id}")
    @ApiOperation(value = "유저 언팔로우", notes = "특정 유저를 언팔로우 한다.")
    public BaseResponseDto<FollowDeleteResponseDto> unfollowUser(@RequestBody FollowDeleteRequestDto requestDto, @PathVariable Long my_id){
        User user = userService.findById(my_id);
        return new BaseResponseDto<>(followService.unfollowUser(requestDto,user));
    }

    @GetMapping("/{my_id}/followings/me")
    @ApiOperation(value = "내 팔로잉 조회", notes = "내가 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getMyFollowings(@PathVariable Long my_id, Pageable pageable){
        User user = userService.findById(my_id);
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getMyFollowing(user,pageable))) ;
    }

    @GetMapping("/{my_id}/followers/me")
    @ApiOperation(value = "내 팔로워 조회", notes = "나를 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getMyFollowers(@PathVariable Long my_id, Pageable pageable){
        User user = userService.findById(my_id);
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getMyFollower(user,pageable)));
    }

    @GetMapping("/{my_id}/followings/users/{user_id}")
    @ApiOperation(value = "유저 팔로잉 조회", notes = "유저가 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getFollowings(@PathVariable Long user_id, @PathVariable Long my_id,Pageable pageable){
        User user = userService.findById(my_id);
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getFollowing(user_id,user,pageable)));
    }

    @GetMapping("/{my_id}/followers/users/{user_id}")
    @ApiOperation(value = "유저 팔로워 조회", notes = "유저를 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getFollowers(@PathVariable Long user_id, @PathVariable Long my_id,Pageable pageable){
        User user = userService.findById(my_id);
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getFollower(user_id,user,pageable)));
    }


}
