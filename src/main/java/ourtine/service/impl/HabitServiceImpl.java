package ourtine.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.S3Uploader;
import ourtine.converter.DayConverter;
import ourtine.domain.*;
import ourtine.domain.enums.*;
import ourtine.domain.mapping.HabitSessionFollower;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.domain.mapping.HabitDays;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.HabitHashtag;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;
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

import static ourtine.exception.enums.ResponseMessage.WRONG_HABIT_DELETE;

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

    public Habit findById(Long id) {
        return habitRepository.findById(id).orElseThrow(()->new BusinessException(ResponseMessage.WRONG_HABIT));
    }

    // 습관 개설하기
    @Override
    public HabitCreatePostResponseDto createHabit(HabitCreatePostRequestDto habitCreatePostRequestDto, MultipartFile file, User user) throws IOException {
        Habit habit;
        if (file.isEmpty())
            {throw new IOException(new BusinessException(ResponseMessage.WRONG_HABIT_FILE));}

        Category category = categoryRepository.findByName(habitCreatePostRequestDto.getCategory()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));

        if (habitCreatePostRequestDto.getHabitStatus()== HabitStatus.PUBLIC)
        {
            String imageUrl = s3Uploader.upload(file,"images/habits");

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
            String imageUrl = s3Uploader.upload(file,"images/habits");

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
                hashtag = hashtagRepository.findHashtagByName(name).get();
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

    @Override
    public void saveOrUpdateHabit(Habit habit) {
        habitRepository.save(habit);
    }


    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)
    @Override
    public Slice<HabitMyFollowingListGetResponseDto> getTodaysMyHabits(User user, Pageable pageable) {
        Day day = dayConverter.curDayOfWeek();
        Slice<Long> followingHabitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(user.getId(),pageable);
        Slice<Long> habitIdsOfDay = habitDaysRepository.queryFindFollowingHabitsByDay(followingHabitIds.getContent(),
                    day,pageable);
        Slice<Habit> habitsOfDay = habitRepository.queryFindHabitsOrderByStartTime(habitIdsOfDay.getContent());

        return habitsOfDay.map(habit -> new HabitMyFollowingListGetResponseDto(
                habit,
                userMvpRepository.queryFindByHabitIdAndUserId(habit.getId(),user.getId()).size()
                ));

    }

    // 습관 프로필 조회
    @Override
    public HabitGetResponseDto getHabit(Long habitId, User user) {

        Habit habit = habitRepository.findById(habitId).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT));
        Category category  = categoryRepository.findById(habit.getCategoryId()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));
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

    // 내 프로필 - 참여한 습관 리스트
    public Slice<HabitUserFollowedGetResponseDto> getMyHabits(User user, Pageable pageable){
        Slice<HabitUserFollowedGetResponseDto> responseDto  = null;
        List<Long> habitIds = habitFollowersRepository.queryFindMyHabitIds(user.getId(), pageable).getContent();
        if (habitIds.size()>0){
            Slice<Habit> habits = habitRepository.queryFindHabitsById(habitIds); // public + private

            responseDto = habits.map(habit ->
                    {
                        Category category = categoryRepository.findById(habit.getCategoryId()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));
                        List<String> hashtags = habitHashtagRepository.queryFindHashtagNameByHabit(habit.getId());
                        return new HabitUserFollowedGetResponseDto(habit, category, hashtags);
                    }
            );

        }return responseDto;
    }

    // 내 프로필 - 위클리 로그
    @Override
    public List<HabitWeeklyLogGetResponseDto> getMyWeeklyLog(User user) {
        String monday = dayConverter.getCurMonday();
        List<HabitSessionFollower> habitSessionFollowers = habitSessionFollowerRepository.getFollowerSessionInfo(monday, user.getId());
        List<HabitWeeklyLogGetResponseDto> responseDto = new ArrayList<>();
        habitSessionFollowers.forEach(follower -> {
            responseDto.add(new HabitWeeklyLogGetResponseDto(dayConverter.dayOfWeek(follower.getCreatedAt()),
                    follower.getVideoUrl(), follower.getEmotion()));
        });
        return responseDto;
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @Override
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(Long userId, User me, Pageable pageable) {
        HabitUserFollowingListGetResponseDto responseDto;

        if (userRepository.findById(userId).isPresent()) { // 존재하는 유저면
            if (followRepository.findBySenderIdAndReceiverId(me.getId(), userId).isPresent()) {
                // 친구인 유저면
                Slice<Habit> commonHabits = habitFollowersRepository.queryGetCommonHabitsByUserId(userId, me);
                Slice<Habit> otherHabits = habitFollowersRepository.queryFindOtherHabitsByUserId(userId, me, pageable);
                SliceResponseDto<HabitFollowingInfoDto> commonHabitsInfo = new SliceResponseDto<>(commonHabits.map(habit ->{
                    List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());
                    return new HabitFollowingInfoDto(habit,calculatorClass.habitParticipateRate(habit.getId(), followers,habitSessionRepository,
                            habitSessionFollowerRepository));
                }));
                SliceResponseDto<HabitFollowingInfoDto> otherHabitsInfo = new SliceResponseDto<>(otherHabits.map(habit -> {
                            List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());
                            return new HabitFollowingInfoDto(habit, calculatorClass.habitParticipateRate(habit.getId(), followers, habitSessionRepository,
                                    habitSessionFollowerRepository));
                        }));
                responseDto = new HabitUserFollowingListGetResponseDto(userId, true, null, commonHabitsInfo, otherHabitsInfo);
            } else {
                // 친구가 아닌 유저면
                List<Long> habitIds = habitFollowersRepository.queryFindMyFollowingHabitIds(userId, pageable).getContent();
                Slice<Habit> habits = habitRepository.queryFindPublicHabitsById(habitIds, me.getId());
                SliceResponseDto<HabitFollowingInfoDto> habitsInfo = new SliceResponseDto<>(habits.map(habit -> {
                    List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());
                    return new HabitFollowingInfoDto(habit,calculatorClass.habitParticipateRate(habit.getId(), followers,habitSessionRepository,
                            habitSessionFollowerRepository));
                }));
                responseDto = new HabitUserFollowingListGetResponseDto(userId, false, habitsInfo, null, null);
            }
        }
        else {
                throw new BusinessException(ResponseMessage.WRONG_USER);
             }
        return responseDto;
    }

    // 유저 프로필 - 참여했던 습관 목록
    @Override
    public Slice<HabitUserFollowedGetResponseDto> getUserFollowedHabits(Long userId, User me, Pageable pageable) {
        Slice<HabitUserFollowedGetResponseDto> responseDto  = null;
        if (userRepository.findById(userId).isPresent()){
            // 친구인 유저면
            if (followRepository.findBySenderIdAndReceiverId(me.getId(), userId).isPresent()) {
                List<Long> habitIds = habitFollowersRepository.queryFindMyFollowedHabitIds(userId, pageable).getContent();
                if (habitIds.size()>0){
                    Slice<Habit> habits = habitRepository.queryFindHabitsById(habitIds); // public + private

                    responseDto = habits.map(habit ->
                            {
                                Category category = categoryRepository.findById(habit.getCategoryId()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));
                                List<String> hashtags = habitHashtagRepository.queryFindHashtagNameByHabit(habit.getId());
                                return new HabitUserFollowedGetResponseDto(habit, category, hashtags);
                            }
                    );
                }
            }
            // 친구가 아니면
            else {
            List<Long> habitIds = habitFollowersRepository.queryFindMyFollowedHabitIds(userId,pageable).getContent();
                if (habitIds.size()>0){
                    Slice<Habit> habits = habitRepository.queryFindPublicHabitsById(habitIds, me.getId()); // public 습관만
                    Slice<HabitUserFollowedGetResponseDto> habitsResult = habits.map(habit ->
                            {
                                Category category = categoryRepository.findById(habit.getCategoryId()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));
                                List<String> hashtags = habitHashtagRepository.queryFindHashtagNameByHabit(habit.getId());
                                return new HabitUserFollowedGetResponseDto(habit, category, hashtags);
                            }
                    );
                    responseDto = habitsResult;
                }
            }
            return responseDto;

        }
        else {
            throw new BusinessException(ResponseMessage.WRONG_USER);
        }
    }
    // 추천 습관 목록
    @Override
    public Slice<HabitRecommendResponseDto> getRecommendHabits(User user, Pageable pageable) {
        Slice<Habit> habits = habitRepository.queryGetRecommendHabits(user.getId(),pageable);
        Slice<HabitRecommendResponseDto> result = habits.map(habit ->
                new HabitRecommendResponseDto(habit,categoryRepository.findById(habit.getCategoryId()).get(),
                        habitDaysRepository.findDaysByHabit(habit)));
        return result;
    }
    @Transactional
    // 습관 참여하기
    @Override
    public HabitFollowerResponseDto joinHabit(Long habitId, User user) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT));
        //참여하고 있으면 or 인원이 다 찬 상태이면 참여 불가
        if (habitFollowersRepository.findByHabitIdAndFollowerId(habitId,user.getId()).isPresent() ||
                habit.getFollowerLimit()-habit.getFollowerCount()<1 ){
            throw new BusinessException(ResponseMessage.WRONG_HABIT_JOIN);
        }
        else {
            List<Long> followingHabits = habitFollowersRepository.queryFindMyFollowingHabitIds(user.getId(),Pageable.unpaged()).getContent();
            List<Long> sortByDay = new ArrayList<>();

            // 내가 팔로잉 중인 습관들을 소팅하기 ( 신청하려는 습관의 요일과 겹치는 )
            for (HabitDays habitDays: habit.getDays()){
                List<Habit> habits = habitDaysRepository.queryFindHabitsByDay(habitDays.getDay(),followingHabits);
                for (Habit h : habits){
                    if (!sortByDay.contains(h.getId())){
                        sortByDay.add(h.getId());
                    }
                }
            }

            boolean canJoin = true;
            if (!sortByDay.isEmpty()){
                // 시간대가 하나라도 겹치면 참여 불가
                for (Long s : sortByDay) {
                    Habit sort = habitRepository.findById(s).get();
                    if (habitRepository.sortByTime(habitId, sort.getStartTime(), sort.getEndTime()).isEmpty()) {
                        canJoin = false;
                    }
                }
            }
                if(canJoin)
                {
                    HabitFollowers habitFollower = HabitFollowers.builder().follower(user).habit(habit).build();
                    habitFollowersRepository.save(habitFollower);
                    // 습관 참여자 수 업데이트
                    habit.setFollowerCount(habitFollowersRepository.countHabitFollowersByHabit(habit));
                    return new HabitFollowerResponseDto(habitId, user.getId());
                }
                else
                    throw new BusinessException(ResponseMessage.WRONG_HABIT_TIME);
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
                categoryRepository.findById(habit.getCategoryId()).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY))
        ));
    }

    // 카테고리별 검색
    @Override
    public Slice<HabitFindByCategoryGetResponseDto> findHabitsByCategory(CategoryList categoryName, User user, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT_CATEGORY));
        Slice<Habit> habits = habitRepository.querySearchHabitByCategory(user.getId(), category.getId(), pageable);

        return habits.map(habit ->
             new HabitFindByCategoryGetResponseDto(habit, category,habitDaysRepository.findDaysByHabit(habit)));
    }

    // 습관 참여 취소 하기
    @Modifying
    @Transactional
    @Override
    public HabitFollowerResponseDto quitHabit(Long habitId, User user) {
        if(habitRepository.findById(habitId).isEmpty()){
            throw new BusinessException(ResponseMessage.WRONG_HABIT);
        }
        if(habitFollowersRepository.findByHabitIdAndFollowerId(habitId, user.getId()).isEmpty()){
            throw new BusinessException(ResponseMessage.WRONG_HABIT_QUIT);
        }
        else
        {
            habitFollowersRepository.queryDeleteFollowerById(habitId, user);
        }
        return new HabitFollowerResponseDto(habitId,user.getId());
    }

    // 습관 위클리 로그
    @Override
    public Slice<HabitDailyLogGetResponseDto> getHabitWeeklyLog(Long habitId, User user) {
        if (habitRepository.findById(habitId).isEmpty()){
            throw new BusinessException(ResponseMessage.WRONG_HABIT);
        }
        return habitSessionFollowerRepository.
                findByFollowerIdAndHabitSessionHabitId(user.getId(),habitId).map(
                        review-> new HabitDailyLogGetResponseDto(review.getHabitSession().getDate(), review.getEmotion()));
    }

    // 습관 초대장
    @Override
    public HabitInvitationPostResponseDto sendInvitation(User me, HabitInvitationPostRequestDto requestDto){
        List<Long> friends = requestDto.getFriends();
        friends.forEach(friend ->{
            User receiver = userRepository.findById(friend).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_USER));
                if (followRepository.findBySenderIdAndReceiverId(me.getId(),friend).isPresent()) {
                    Message invitation = NewMessage.builder().messageType(MessageType.HABIT_INVITE)
                            .sender(me).receiver(receiver).contents(requestDto.getHabitId().toString()).build();
                    messageRepository.save(invitation);
                }
        }
        );
        return new HabitInvitationPostResponseDto();

    }

    // 습관 삭제하기
    @Transactional
    @Modifying
    @Override
    public HabitDeleteResponseDto deleteHabit(Long habitId, User user){
        Habit habit = habitRepository.findById(habitId).orElseThrow(()-> new BusinessException(ResponseMessage.WRONG_HABIT));
        // 습관 개설자와 삭제하려는 유저가 같다면
        if (habit.getHost().getId().equals(user.getId())) {
            // 습관-요일 삭제
            habitDaysRepository.deleteByHabitId(habit.getId());

            // 이 게시물의 해시태그가 사용되는 게시물이 없다면, 해시태그 삭제
            habitHashtagRepository.findByHabit(habit).forEach(
                    hh->{
                        if(habitHashtagRepository.countByHashtag(hh.getHashtag())==1){
                            habitHashtagRepository.deleteByHabitId(habit.getId());
                            hashtagRepository.deleteById(hh.getHashtag().getId());
                        }
                        else {
                            habitHashtagRepository.deleteByHabitId(habit.getId());
                        }
                    }
            );
            
            // 습관-팔로워 삭제
            habitFollowersRepository.deleteByHabitId(habit.getId());

            // 습관-세션-팔로워 삭제
            habitSessionFollowerRepository.deleteByHabitSession_Habit_Id(habit.getId());

            // 습관-세션 삭제
            habitSessionRepository.deleteByHabitId(habit.getId());

            habitRepository.deleteById(habit.getId());

            return new HabitDeleteResponseDto(habitId, user.getId());
        }
        else throw new BusinessException(WRONG_HABIT_DELETE);
    }

}