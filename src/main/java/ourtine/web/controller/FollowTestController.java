package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ourtine.domain.User;
import ourtine.domain.common.SliceResponseDto;
import ourtine.repository.UserRepository;
import ourtine.service.impl.FollowServiceImpl;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.FollowDeleteResponseDto;
import ourtine.web.dto.response.FollowPostResponseDto;
import ourtine.web.dto.response.FollowersGetResponseDto;
import ourtine.web.dto.response.FollowingsGetResponseDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow/test")
public class FollowTestController {
    private final FollowServiceImpl followService;
    private final UserRepository userRepository;

    @PostMapping
    @ApiOperation(value = "유저 팔로우", notes = "특정 유저를 팔로우 한다.")
    public FollowPostResponseDto followUser(@RequestBody FollowPostRequestDto requestDto, User user){
        User dana = userRepository.findById(1l).get();
        return followService.followUser(requestDto,dana);
    }
    @DeleteMapping
    @ApiOperation(value = "유저 언팔로우", notes = "특정 유저를 언팔로우 한다.")
    public FollowDeleteResponseDto unfollowUser(@RequestBody FollowDeleteRequestDto requestDto, User user){
        User dana = userRepository.findById(1l).get();
        return followService.unfollowUser(requestDto,dana);
    }

    @GetMapping("/followings/me")
    @ApiOperation(value = "내 팔로잉 조회", notes = "내가 팔로우하는 유저를 조회한다.")
    public SliceResponseDto<FollowingsGetResponseDto> getMyFollowings(User user, Pageable pageable){
        User dana = userRepository.findById(1l).get();
        return new SliceResponseDto<>(followService.getMyFollowing(dana,pageable));
    }

    @GetMapping("/followers/me")
    @ApiOperation(value = "내 팔로워 조회", notes = "나를 팔로우하는 유저를 조회한다.")
    public SliceResponseDto<FollowersGetResponseDto> getMyFollowers(User user, Pageable pageable){
        User dana = userRepository.findById(1l).get();
        return new SliceResponseDto<>(followService.getMyFollower(dana,pageable));
    }

    @GetMapping("/followings/users/{user_id}")
    @ApiOperation(value = "유저 팔로잉 조회", notes = "유저가 팔로우하는 유저를 조회한다.")
    public SliceResponseDto<FollowingsGetResponseDto> getFollowings(@PathVariable Long user_id, User user,Pageable pageable){
        User dana = userRepository.findById(1l).get();
        return new SliceResponseDto<>(followService.getFollowing(user_id,dana,pageable));
    }

    @GetMapping("/followings/users/{user_id}")
    @ApiOperation(value = "유저 팔로워 조회", notes = "유저를 팔로우하는 유저를 조회한다.")
    public SliceResponseDto<FollowersGetResponseDto> getFollowers(@PathVariable Long user_id, User user,Pageable pageable){
        User dana = userRepository.findById(1l).get();
        return new SliceResponseDto<>(followService.getFollower(user_id,dana,pageable));
    }


}
