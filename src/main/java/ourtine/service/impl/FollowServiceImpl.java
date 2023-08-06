package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Follow;
import ourtine.domain.User;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;
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
            if (followRepository.findBySenderIdAndReceiverId(me.getId(), requestDto.getUserId()).isPresent()) {
                return new FollowGetResponseDto(requestDto.getUserId(), true);
            }
            else  return new FollowGetResponseDto(requestDto.getUserId(), false);
        }
        else throw new BusinessException(ResponseMessage.WRONG_USER); // 에러 처리
    }

    // 팔로우 하기
    @Override
    @Transactional
    public FollowPostResponseDto followUser(FollowPostRequestDto requestDto, User me) {
        User receiver = userRepository.findById(requestDto.getUserId()).orElseThrow(()->
                new BusinessException(ResponseMessage.WRONG_USER));
        // 이미 팔로우하고 있으면
        if (followRepository.findBySenderIdAndReceiverId(me.getId(), requestDto.getUserId()).isPresent()){
            throw new BusinessException(ResponseMessage.WRONG_FOLLOW);
        }
        else {
            followRepository.save(Follow.builder().sender(me).receiver(receiver).build());
        }
        return new FollowPostResponseDto(requestDto.getUserId());
    }

    // 언팔로우 하기
    @Override
    @Modifying
    @Transactional
    public FollowDeleteResponseDto unfollowUser(FollowDeleteRequestDto requestDto, User me) {
        userRepository.findById(requestDto.getUserId()).orElseThrow(
                ()->new BusinessException(ResponseMessage.WRONG_USER));
        // 팔로우하고 있지 않으면
        if (followRepository.findBySenderIdAndReceiverId(me.getId(), requestDto.getUserId()).isEmpty()){
            throw new BusinessException(ResponseMessage.WRONG_UNFOLLOW);
        }
        else {
            Follow follow = followRepository.findBySenderIdAndReceiverId(me.getId(), requestDto.getUserId()).get();
            followRepository.delete(follow);
        }
        return new FollowDeleteResponseDto(requestDto.getUserId());
    }

    // 내 팔로잉 목록
    @Override
    public Slice<FollowingsGetResponseDto> getMyFollowing(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findBySenderIdOrderByCreatedAtDesc(me.getId());
        Slice<FollowingsGetResponseDto> followings = result.map(following ->
                new FollowingsGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                        following.getReceiver().getImageUrl()));
        return followings;
    }

    // 내 팔로워 목록
    @Override
    public Slice<FollowersGetResponseDto> getMyFollower(User me, Pageable pageable) {
        Slice<Follow> result = followRepository.findByReceiverIdOrderByCreatedAtDesc(me.getId());
        Slice<FollowersGetResponseDto> followers = result.map(following ->
                new FollowersGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }

    // 유저 팔로잉 목록
    @Override
    public Slice<FollowingsGetResponseDto> getFollowing(Long userId, User me, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(
                ()->new BusinessException(ResponseMessage.WRONG_USER));
        Slice<Follow> result = followRepository.queryFindBySenderIdOrderByCreatedAtDesc(userId, me.getId());
        Slice<FollowingsGetResponseDto> followings = result.map(following ->
            new FollowingsGetResponseDto(following.getReceiver().getId(),following.getReceiver().getNickname(),
                    following.getReceiver().getImageUrl()));
        return followings;
    }

    // 유저 팔로워 목록
    @Override
    public Slice<FollowersGetResponseDto> getFollower(Long userId, User me, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(
                ()->new BusinessException(ResponseMessage.WRONG_USER));
        Slice<Follow> result = followRepository.queryFindByReceiverIdOrderByCreatedAtDesc(userId, me.getId());
        Slice<FollowersGetResponseDto> followers = result.map(following ->
                new FollowersGetResponseDto(following.getSender().getId(),following.getSender().getNickname(),
                        following.getSender().getImageUrl()));
        return followers;
    }



}
