package ourtine.util;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ourtine.domain.User;
import ourtine.domain.mapping.HabitSessionFollower;
import ourtine.repository.HabitFollowersRepository;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public final class CalculatorClass {

    public int myHabitParticipateRate(Long habitId, User user,
                                      HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                      HabitFollowersRepository habitFollowersRepository){
        int participateRate = 0;
        // 참여 날짜
        LocalDate joinDate = habitFollowersRepository.findByHabitIdAndFollowerId(habitId, user.getId()).get().getCreatedAt().toLocalDate();
        // 해당 습관의 종료된 세션 목록
        List<Long> sessionIds = habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId,java.sql.Date.valueOf(joinDate));
        // 참여한 날짜 이후로 진행 된 세션이 하나라도 있으면 참여율 산출
        if (sessionIds.size()>0){
            // 유저가 세션에 참여한 횟수
            Long participatedSessions = habitSessionFollowerRepository.queryGetParticipateSessionNumber(user, habitId, sessionIds);
            // 내 참여율
            participateRate = Math.round((float) participatedSessions / sessionIds.size()) * 100;
        }
        return participateRate;
    }

    public int myParticipateRate(User user,
                                 HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                 HabitFollowersRepository habitFollowersRepository){
        int participateRate = 0;
        List<Long> habitIds = habitFollowersRepository.queryFindMyHabitIds(user.getId(), Pageable.unpaged()).getContent();
        if (habitIds.size()>0){
            int participateRateSum = 0;
            for (Long habitId : habitIds) {
                // 내 습관 참여도 합
                participateRateSum += myHabitParticipateRate(habitId,user,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository);
            }
            participateRate = Math.round((float) participateRateSum / habitIds.size()) ;
        }
        return participateRate;

    }

    public int habitParticipateRate(Long habitId,List<User> followers,
                                    HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                    HabitFollowersRepository habitFollowersRepository){
        int participateRate = 0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 유저들의 참여도 합
            int participateRateSum =0;
            for (User follower : followers){
                participateRateSum += myHabitParticipateRate(habitId,follower,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository);
            }
            participateRate = Math.round((float) participateRateSum / followers.size());
        }
        return participateRate;
    }

    public double myHabitStarRate(Long habitId, User user, HabitSessionFollowerRepository habitSessionFollowerRepository){
        double starRate = 0;
        double starRateSum = 0;
        int reviewSize = 0;
        List<HabitSessionFollower> reviewInfos = habitSessionFollowerRepository.findByFollowerIdAndHabitSessionHabitId(user.getId(), habitId).getContent();
        if (reviewInfos.size()>0){
            for (HabitSessionFollower reviewInfo :reviewInfos ) {
                if (!(reviewInfo.getStarRate()==null)){
                    // 내 습관 만족도 합
                    starRateSum += reviewInfo.getStarRate();
                    reviewSize += 1;
                }

            }
            starRate = (double) Math.round(starRateSum / reviewSize * 10) /10;
        }

        return starRate;

    }

    public double habitStarRate(Long habitId, List<User> followers,
                                HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                HabitFollowersRepository habitFollowersRepository){
        double starRate = 0.0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 유저들의 참여한 횟수
            double starRateSum = 0.0;
            for (User follower : followers){
                // 유저가 참여한 세션의 만족도
                starRateSum += myHabitStarRate(habitId,follower,habitSessionFollowerRepository);
            }
            // 소수점 첫째 자리까지 출력
            starRate = (double) Math.round(starRateSum / followers.size() * 10) /10;
        }
        return starRate;
    }
}
