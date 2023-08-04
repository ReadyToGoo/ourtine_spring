package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Follow;
import ourtine.domain.User;
import ourtine.repository.FollowRepository;
import ourtine.repository.UserRepository;
import ourtine.service.FollowService;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowGetRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowServiceImpl implements FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

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
    public FollowPostResponseDto followUser(FollowPostRequestDto requestDto, User me) {
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
    public FollowDeleteResponseDto unfollowUser(FollowDeleteRequestDto requestDto, User me) {
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
    public Slice<FollowingsGetResponseDto> getMyFollowing(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findBySenderOrderByCreatedAtDesc(me);
        Slice<FollowingsGetResponseDto> followings = result.map(following ->
                new FollowingsGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                        following.getReceiver().getImageUrl()));
        return followings;
    }

    // 내 팔로워 목록
    @Override
    public Slice<FollowersGetResponseDto> getMyFollower(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findByReceiverOrderByCreatedAtDesc(me);
        Slice<FollowersGetResponseDto> followers = result.map(following ->
                new FollowersGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }

    // 유저 팔로잉 목록
    @Override
    public Slice<FollowingsGetResponseDto> getFollowing(Long userId, User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findBySenderIdOrderByCreatedAtDesc(userId);
        Slice<FollowingsGetResponseDto> followings = result.map(following ->
            new FollowingsGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                    following.getReceiver().getImageUrl()));
        return followings;
    }

    // 유저 팔로워 목록
    @Override
    public Slice<FollowersGetResponseDto> getFollower(Long userId, User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        Slice<FollowersGetResponseDto> followers = result.map(following ->
                new FollowersGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }



}
