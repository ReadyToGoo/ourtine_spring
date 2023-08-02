package ourtine.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.web.dto.request.HabitSessionMvpVotePostRequestDto;
import ourtine.web.dto.request.HabitSessionReviewPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.List;

@Service
public interface HabitSessionService {

    // @Transactional
    public HabitSessionGetResponseDto getHabitSession(Long habitId, User user);

    // @Transactional
    public HabitSessionEnterPostResponseDto enterHabitSession(Long sessionId, User user);

    @Transactional
    public HabitSessionUploadVideoPostResponseDto uploadVideo(Long sessionId, MultipartFile file ,User user) throws IOException;

    @Transactional
    public HabitSessionMvpVotePostResponseDto voteMvp(Long sessionId, User user, HabitSessionMvpVotePostRequestDto habitSessionMvpVotePostRequestDto);

    // @Transactional
    public HabitSessionMvpCandidateGetResponseDto getMvpCandidateList(Long sessionId);

    public List<HabitSessionMvpGetResponseDto> showMvp(Long sessionId, User user);

    @Transactional
    public HabitSessionReviewPostResponseDto writeReview(Long sessionId, HabitSessionReviewPostRequestDto requestDto, User user);

}