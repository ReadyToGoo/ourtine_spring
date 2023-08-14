package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ourtine.converter.MessageContentsConverter;
import ourtine.domain.NewMessage;
import ourtine.domain.User;
import ourtine.domain.UserDetailsImpl;
import ourtine.domain.enums.MessageType;
import ourtine.service.MessageService;
import ourtine.service.UserService;
import ourtine.web.dto.common.BaseResponseDto;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.service.impl.FollowServiceImpl;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowServiceImpl followService;
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "유저 팔로우", notes = "특정 유저를 팔로우 한다.")
    public BaseResponseDto<FollowPostResponseDto> followUser(@RequestBody FollowPostRequestDto requestDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        FollowPostResponseDto followPostResponseDto = followService.followUser(requestDto, user);
        User receiver = userService.findById(requestDto.getUserId());
        messageService.newFollowMessage(user, receiver);
        return new BaseResponseDto<>(followPostResponseDto);
    }
    @DeleteMapping
    @ApiOperation(value = "유저 언팔로우", notes = "특정 유저를 언팔로우 한다.")
    public BaseResponseDto<FollowDeleteResponseDto> unfollowUser(@RequestBody FollowDeleteRequestDto requestDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(followService.unfollowUser(requestDto,user));
    }

    @GetMapping("/followings/me")
    @ApiOperation(value = "내 팔로잉 조회", notes = "내가 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getMyFollowings( Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getMyFollowing(user,pageable))) ;
    }

    @GetMapping("/followers/me")
    @ApiOperation(value = "내 팔로워 조회", notes = "나를 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getMyFollowers( Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getMyFollower(user,pageable)));
    }

    @GetMapping("/followings/users/{user_id}")
    @ApiOperation(value = "유저 팔로잉 조회", notes = "유저가 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getFollowings(@PathVariable Long user_id, Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getFollowing(user_id,user,pageable)));
    }

    @GetMapping("/followers/users/{user_id}")
    @ApiOperation(value = "유저 팔로워 조회", notes = "유저를 팔로우하는 유저를 조회한다.")
    public BaseResponseDto<SliceResponseDto<UserSimpleProfileResponseDto>> getFollowers(@PathVariable Long user_id,Pageable pageable){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return new BaseResponseDto<>(new SliceResponseDto<>(followService.getFollower(user_id,user,pageable)));
    }


}
