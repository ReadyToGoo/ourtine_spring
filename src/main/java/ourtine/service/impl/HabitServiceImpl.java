package ourtine.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.S3Uploader;
import ourtine.converter.DayConverter;
import ourtine.domain.*;
import ourtine.domain.common.SliceResponseDto;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.HabitStatus;
import ourtine.domain.enums.MessageType;
import ourtine.domain.enums.Sort;
import ourtine.domain.mapping.HabitDays;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.HabitHashtag;
import ourtine.repository.*;
import ourtine.util.CalculatorClass;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import ourtine.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ourtine.web.dto.request.HabitInvitationPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitDaysRepository habitDaysRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;
    private final HabitHashtagRepository habitHashtagRepository;
    private final S3Uploader s3Uploader;
    private final MessageRepository messageRepository;
    private final DayConverter dayConverter;
    private final UserMvpRepository userMvpRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final CalculatorClass calculatorClass;

    // 습관 개설하기
    @Override
    public HabitCreatePostResponseDto createHabit(HabitCreatePostRequestDto habitCreatePostRequestDto, MultipartFile file, User user) throws IOException {
        Habit habit;
        Category category = categoryRepository.findByName(habitCreatePostRequestDto.getCategory()).orElseThrow();

        if (habitCreatePostRequestDto.getHabitStatus()== HabitStatus.PUBLIC)
        {
            String imageUrl = s3Uploader.upload(file,"");

            habit = PublicHabit.builder()
                    .host(user)
                    .title(habitCreatePostRequestDto.getTitle())
                    .detail(habitCreatePostRequestDto.getDetail())
                    .imageUrl(imageUrl)
                    .categoryId(category.getId())
                    .startTime(habitCreatePostRequestDto.getStartTime())
                    .endTime(habitCreatePostRequestDto.getEndTime())
                    .startDate(habitCreatePostRequestDto.getStartDate())
                    .endDate(habitCreatePostRequestDto.getEndDate())
                    .followerLimit(habitCreatePostRequestDto.getFollowerLimit())
                    .build();
        }
        else if (habitCreatePostRequestDto.getHabitStatus()==HabitStatus.PRIVATE){
            String imageUrl = s3Uploader.upload(file,"");

            habit = PrivateHabit.builder()
                    .host(user)
                    .title(habitCreatePostRequestDto.getTitle())
                    .detail(habitCreatePostRequestDto.getDetail())
                    .imageUrl(imageUrl)
                    .categoryId(category.getId())
                    .startTime(habitCreatePostRequestDto.getStartTime())
                    .endTime(habitCreatePostRequestDto.getEndTime())
                    .startDate(habitCreatePostRequestDto.getStartDate())
                    .endDate(habitCreatePostRequestDto.getEndDate())
                    .followerLimit(habitCreatePostRequestDto.getFollowerLimit())
                    .build();
        }
        else return null;

        Habit savedHabit = habitRepository.save(habit);
        Long habitNum = habitRepository.countByHost(user);

        habitCreatePostRequestDto.getDays().forEach(day ->{
            HabitDays habitDays = HabitDays.builder().habit(savedHabit).day(day).build();
            habitDaysRepository.save(habitDays);
        });

        // 해시태그 DB에 저장
        habitCreatePostRequestDto.getHashtags().forEach(name->{
            Hashtag hashtag;
            if (hashtagRepository.existsByName(name)){
                hashtag = hashtagRepository.findHashtagByName(name).orElseThrow();
            }
            else {
                hashtag = Hashtag.builder().name(name).build();
                hashtagRepository.saveAndFlush(hashtag);
            }
            // 해시태그 매핑테이블에 저장
            HabitHashtag habitHashtag = HabitHashtag.builder().habit(savedHabit).hashtag(hashtag).build();
            habitHashtagRepository.save(habitHashtag);
        });

        // 팔로워 매핑테이블에 호스트 저장
        HabitFollowers habitFollowers = HabitFollowers.builder().follower(user).habit(habit).build();
        habitFollowersRepository.save(habitFollowers);

        return new HabitCreatePostResponseDto(savedHabit.getId(),habitNum);
    }


    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)
    @Override
    public Slice<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user, Pageable pageable) {
        Day day = dayConverter.dayOfWeek();
        Slice<Long> followingHabitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(user.getId(),pageable);
        if (followingHabitIds.isEmpty()){} // TODO: 에러
        Slice<Long> habitIdsOfDay = habitDaysRepository.queryFindFollowingHabitsByDay(followingHabitIds.getContent(),day,pageable);
        Slice<Habit> habitsOfDay = habitRepository.queryFindHabitsById(habitIdsOfDay.getContent());

        return habitsOfDay.map(habit -> new HabitMyFollowingListGetResponseDto(
                habit,
                userMvpRepository.findByHabitSessionHabitAndUser(habit,user).size()
                ));

    }

    // 습관 프로필 조회
    @Override
    public HabitGetResponseDto getHabit(Long habitId, User user) {

        Habit habit = habitRepository.findById(habitId).orElseThrow();
        Category category  = categoryRepository.findById(habit.getCategoryId()).orElseThrow();
        List<String> hashtags= habitHashtagRepository.queryFindHashtagNameByHabit(habitId);
        List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habitId);
        List<HabitFollowersGetResponseDto> habitFollowersResult = new ArrayList<>();
        for (User follower : followers){
            habitFollowersResult.add(new HabitFollowersGetResponseDto(follower.getId(),habitRepository.existsByHostAndId(user, habitId)
                    ,follower.getNickname(),follower.getImageUrl()));
        }

        // 참여하고 있으면
        if(habitFollowersRepository.findByHabitIdAndFollowerId(habitId, user.getId()).isPresent()){
            int participateRate = calculatorClass.myHabitParticipateRate(habitId,user,habitSessionRepository,habitSessionFollowerRepository);
            return new HabitGetResponseDto(true,null,new HabitFollowingGetResponseDto(habit,hashtags,category,participateRate,habitFollowersResult));
        }
        // 참여 안 하고 있으면
        else {
            int participateRate = calculatorClass.habitParticipateRate(habitId,followers,habitSessionRepository,habitSessionFollowerRepository);
            double starRate = calculatorClass.starRate(habitId,followers,habitSessionRepository,habitSessionFollowerRepository);
            return new HabitGetResponseDto(false, new HabitNotFollowingGetResponseDto(habit,participateRate, starRate, hashtags, category, habitFollowersResult,
                    habitRepository.queryGetHabitRecruitingStatus(habitId)), null);
        }

    }


    // 유저 프로필 - 팔로잉 하는 습관 목록
    @Override
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(Long userId, User me, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(); // 에러 처리
        HabitUserFollowingListGetResponseDto responseDto = null;

        if (followRepository.findBySenderAndReceiverId(me,userId).isPresent()) {
            // 친구인 유저면과
            Slice<Habit> commonHabits = habitFollowersRepository.queryGetCommonHabitsByUserId(userId, me.getId());
            Slice<Habit> otherHabits = habitFollowersRepository.queryFindOtherHabitsByUserId(userId, me.getId(), pageable);
            SliceResponseDto<HabitFollowingInfoDto> commonHabitsInfo = new SliceResponseDto<>(commonHabits.map(HabitFollowingInfoDto::new));
            SliceResponseDto<HabitFollowingInfoDto> otherHabitsInfo = new SliceResponseDto<>(otherHabits.map(HabitFollowingInfoDto::new));
            responseDto = new HabitUserFollowingListGetResponseDto(true, null, commonHabitsInfo, otherHabitsInfo);
        }
        else {
            // 친구가 아닌 유저면
            List<Long> habitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(userId,pageable).toList();
            Slice<Habit> habits = habitRepository.queryFindPublicHabitsById(habitIds);
            SliceResponseDto<HabitFollowingInfoDto> habitsInfo=  new SliceResponseDto<>(habits.map(HabitFollowingInfoDto::new));
            responseDto = new HabitUserFollowingListGetResponseDto(false,habitsInfo,null,null);
        }

        return responseDto;
    }

    // 추천 습관 목록
    @Override
    public Slice<HabitRecommendResponseDto> getRecommendHabits(User user, Pageable pageable) {
        Slice<Habit> habits = habitRepository.queryGetRecommendHabits(user,pageable);
        Slice<HabitRecommendResponseDto> result = habits.map(habit ->
                new HabitRecommendResponseDto(habit,categoryRepository.findById(habit.getCategoryId()).get(),
                        habitDaysRepository.findDaysByHabit(habit)));
        return result;
    }

    // 습관 참여하기
    @Override
    public HabitFollowerResponseDto joinHabit(Long habitId, User user) {
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        HabitFollowers habitFollower = HabitFollowers.builder().follower(user).habit(habit).build();
        //참여하고 있으면
        if (habitFollowersRepository.findByHabitIdAndFollowerId(habitId,user.getId()).isPresent() ||
                habit.getFollowerLimit()-habit.getFollowerCount()<1 ){
            //에러 처리
            return null;
        }
        else {
            habitFollowersRepository.save(habitFollower);
            // 습관 참여자 수 업데이트
            habit.setFollowerCount(habitFollowersRepository.countHabitFollowersByHabit(habit));
            return new HabitFollowerResponseDto(habitId, user.getId());
        }

    }


    // 습관 검색하기
    @Override
    public Slice<HabitSearchResponseDto> searchHabits(Sort sort, User user, String keyword, Pageable pageable) {
        Slice<Habit> habits;
        // 키워드와 일치하는 해시태그를 가진 습관 아이디 리스트
        List<Long> habitsIdsSearchByHashtag = habitHashtagRepository.queryFindHabitIdsByHashtag(habitHashtagRepository.queryFindHashTagIdsByName(keyword).getContent()).getContent();

        if (sort == Sort.CREATED_DATE){
            habits = habitRepository.queryFindHabitOrderByCreatedAt(user.getId(), keyword, habitsIdsSearchByHashtag,pageable);
        }
        else if (sort == Sort.START_DATE){
            habits = habitRepository.queryFindHabitOrderByStartDate(user.getId(), keyword, habitsIdsSearchByHashtag, pageable);
        }
        else if (sort == Sort.RECRUITING){
            habits = habitRepository.querySearchFindOrderByFollowerCount(user.getId(), keyword, habitsIdsSearchByHashtag,pageable);
        }
        else return null;

        return habits.map(habit -> new HabitSearchResponseDto(
                habit,
                habitRepository.queryGetHabitRecruitingStatus(habit.getId()),
                habitHashtagRepository.queryFindHashtagNameByHabit(habit.getId()),
                categoryRepository.findById(habit.getCategoryId()).orElseThrow()
        ));
    }

    // 카테고리별 검색
    @Override
    public Slice<HabitFindByCategoryGetResponseDto> findHabitsByCategory(String categoryName, User user, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(); // 에러 처리
        Slice<Habit> habits = habitRepository.querySearchHabitByCategory(user.getId(), category.getId(), pageable);
        return habits.map(habit ->
             new HabitFindByCategoryGetResponseDto(habit, category,habitDaysRepository.findDaysByHabit(habit)));
    }

    // 습관 참여 취소 하기
    @Override
    public HabitFollowerResponseDto quitHabit(Long habitId, User user) {
        if(habitRepository.findById(habitId).isEmpty()){}
        if(habitFollowersRepository.findByHabitIdAndFollowerId(habitId, user.getId()).isEmpty()){}

        habitFollowersRepository.queryDeleteFollowerById(habitId,user);
        return new HabitFollowerResponseDto(habitId,user.getId());
    }

    // 습관 위클리 로그
    @Override
    public Slice<HabitWeeklyLogResponseDto> getHabitWeeklyLog(Long habitId, User user) {
        if (habitRepository.findById(habitId).isEmpty()){} // 에러
        return habitSessionFollowerRepository.
                findByFollowerIdAndHabitSessionHabitId(user.getId(),habitId).map(
                        review-> new HabitWeeklyLogResponseDto(review.getHabitSession().getDate(), review.getEmotion())); // 에러 처리
    }

    // 습관 초대장
    @Override
    public HabitInvitationPostResponseDto sendInvitation(User me, HabitInvitationPostRequestDto requestDto){
        List<Long> friends = requestDto.getFriends();
        friends.forEach(friend ->{
            User receiver = userRepository.findById(friend).orElseThrow();
                if (followRepository.findBySenderAndReceiverId(me,friend).isPresent()) {
                    Message invitation = NewMessage.builder().messageType(MessageType.HABITINVITE)
                            .sender(me).receiver(receiver).contents(requestDto.getHabitId().toString()).build();
                    messageRepository.save(invitation);
                }
        }
        );
        return new HabitInvitationPostResponseDto();

    }

    // TODO: 습관 삭제
    // 습관 삭제하기
    @Override
    public HabitDeleteResponseDto deleteHabit(Long habitId, User user){
        // TODO: 에러 처리
        Habit habit = habitRepository.findById(habitId).orElseThrow();
        // 습관 개설자와 삭제하려는 유저가 같다면
        if (habit.getHost().equals(user)) {
            // 습관-요일 삭제
            habitDaysRepository.deleteByHabit(habit);

            // 습관-해시태그 삭제
            habitHashtagRepository.deleteByHabit( habit);


            // 이 게시물의 해시태그가 사용되는 게시물이 없다면, 해시태그 삭제
            habitHashtagRepository.queryFindHashtagIdsByHabit(habitId).forEach(
                    id->{
                        if(habitHashtagRepository.countHabitHashtagByHashtagId(id)==0){
                            hashtagRepository.deleteById(id);
                        }
                    }
            );


            // 습관-팔로워 삭제
            habitFollowersRepository.deleteByHabit(habit);

            // 습관-세션-팔로워 삭제
            habitSessionFollowerRepository.deleteByHabitSession_Habit(habit);

            // 습관-세션 삭제
            habitSessionRepository.deleteByHabit(habit);

            habitHashtagRepository.deleteById(habitId);

            return new HabitDeleteResponseDto(habitId, user.getId());
        }
        else return null;
    }

}