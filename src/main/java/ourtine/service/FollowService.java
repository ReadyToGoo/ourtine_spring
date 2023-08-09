package ourtine.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.User;
import ourtine.web.dto.request.FollowDeleteRequestDto;
import ourtine.web.dto.request.FollowGetRequestDto;
import ourtine.web.dto.request.FollowPostRequestDto;
import ourtine.web.dto.response.*;

@Service
public interface FollowService {

    // 팔로우 여부
    public FollowGetResponseDto getFollowStatus(Long userId, Long myId);
    // 팔로우 하기
    @Transactional
    public FollowPostResponseDto followUser(FollowPostRequestDto requestDto, User me);

    @Modifying
    @Transactional
    public FollowDeleteResponseDto unfollowUser(FollowDeleteRequestDto requestDto, User me);

    // 내 팔로잉 목록 보여주기
    public Slice<UserSimpleProfileResponseDto> getMyFollowing(User me, Pageable pageable);

    // 내 팔로워 목록 보여주기
    public Slice<UserSimpleProfileResponseDto> getMyFollower(User me, Pageable pageable);

    // 유저 팔로잉 목록 보여주기
    public Slice<UserSimpleProfileResponseDto> getFollowing(Long userId, User me, Pageable pageable);

    // 유저 팔로워 목록 보여주기
    public Slice<UserSimpleProfileResponseDto> getFollower(Long userId, User me, Pageable pageable);

    // 내 팔로잉 수 조회
    public Long getMyFollowingCount(User me, Pageable pageable);

    // 내 팔로워 수 조회
    public Long getMyFollowerCount(User me, Pageable pageable);

    // 유저 팔로잉 수 조회
    public Long getFollowingCount(Long userId, User me, Pageable pageable);

    // 유저 팔로워 수 조회
    public Long getFollowerCount(Long userId, User me, Pageable pageable);

    }
