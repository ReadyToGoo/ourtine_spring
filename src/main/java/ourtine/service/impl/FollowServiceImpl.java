package ourtine.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ourtine.domain.Follow;
import ourtine.domain.User;
import ourtine.repository.FollowRepository;
import ourtine.repository.UserRepository;
import ourtine.service.FollowService;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowGetRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.*;

public class FollowServiceImpl implements FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowServiceImpl(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    // 팔로우 여부 조회
    @Override
    public FollowGetResponseDto getFollowStatus(FollowGetRequestDto requestDto, User me) {
        if (userRepository.findById(requestDto.getUserId()).isPresent()){
            if (followRepository.findBySenderAndReceiverId(me, requestDto.getUserId()).isPresent()) {
                return new FollowGetResponseDto(requestDto.getUserId(), true);
            }
            else  return new FollowGetResponseDto(requestDto.getUserId(), false);
        }
        else return null; // 에러 처리
    }

    // 팔로우 하기
    @Override
    public FollowPostResponseDto follow(FollowPostRequestDto requestDto, User me) {
        User receiver = userRepository.findById(requestDto.getUserId()).orElseThrow();
        // 이미 팔로우하고 있으면
        if (followRepository.findBySenderAndReceiverId(me, requestDto.getUserId()).isPresent()){
            // 에러 처리
        }
        else {
            followRepository.save(Follow.builder().sender(me).receiver(receiver).build());
        }
        return new FollowPostResponseDto(requestDto.getUserId());
    }

    // 언팔로우 하기
    @Override
    public FollowDeleteResponseDto unFollow(FollowDeleteRequestDto requestDto, User me) {
        User following = userRepository.findById(requestDto.getUserId()).orElseThrow();
        // 팔로우하고 있지 않으면
        if (followRepository.findBySenderAndReceiverId(me, requestDto.getUserId()).isEmpty()){
            // 에러 처리
        }
        else {
            Follow follow = followRepository.findBySenderAndReceiverId(me, requestDto.getUserId()).get();
            followRepository.delete(follow);
        }
        return new FollowDeleteResponseDto(requestDto.getUserId());
    }

    // 내 팔로잉 목록
    @Override
    public Slice<FollowingGetResponseDto> getMyFollowing(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findBySenderOrderByCreatedAtDesc(me);
        Slice<FollowingGetResponseDto> followings = result.map( following ->
                new FollowingGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                        following.getReceiver().getImageUrl()));
        return followings;
    }

    // 내 팔로워 목록
    @Override
    public Slice<FollowerGetResponseDto> getMyFollower(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findByReceiverOrderByCreatedAtDesc(me);
        Slice<FollowerGetResponseDto> followers = result.map( following ->
                new FollowerGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }

    // 유저 팔로잉 목록
    @Override
    public Slice<FollowingGetResponseDto> getFollowing(Long userId, User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findBySenderIdOrderByCreatedAtDesc(userId);
        Slice<FollowingGetResponseDto> followings = result.map( following ->
            new FollowingGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                    following.getReceiver().getImageUrl()));
        return followings;
    }

    // 유저 팔로워 목록
    @Override
    public Slice<FollowerGetResponseDto> getFollower(Long userId, User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        Slice<FollowerGetResponseDto> followers = result.map( following ->
                new FollowerGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }



}
