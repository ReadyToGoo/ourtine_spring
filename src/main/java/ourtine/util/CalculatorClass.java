package ourtine.util;

import org.springframework.stereotype.Component;
import ourtine.domain.User;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;

import java.util.List;

@Component
public final class CalculatorClass {

    public int myParticipateRate(User user,
                                 HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository){
            int participateRate = 0;
            // 진행 된 세션이 하나라도 있으면 참여율 산출
            if (habitSessionRepository.queryCountEndSessionsByUser(user)>0){
                // 유저가 참여한 세션 횟수
                Long participatedSessions = habitSessionFollowerRepository.queryFindEndSessionsByUser(user.getId());

                participateRate = Math.round((float) participatedSessions / habitSessionRepository.queryCountEndSessionsByUser(user)) * 100;
            }
            return participateRate;

    }

    public int myHabitParticipateRate(Long habitId, User user,
                                      HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository){
        int participateRate = 0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 해당 습관의 종료된 세션 목록
            List<Long> sessionIds = habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId);
            // 유저가 세션에 참여한 횟수
            Long participatedSessions = habitSessionFollowerRepository.queryGetParticipateSessionNumber(user, habitId, sessionIds);
            // 내 참여율
            participateRate = Math.round((float) participatedSessions / sessionIds.size()) * 100;
        }
        return participateRate;
    }

    public int habitParticipateRate(Long habitId,List<User> followers,
                                    HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository){
        int participateRate = 0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 해당 습관의 종료된 세션 목록
            List<Long> sessionIds = habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId);

            // 유저들의 참여한 횟수
            long participatedSessionsSum =0l;
            for (User follower : followers){
                // 유저가 세션에 참여한 횟수
                Long participatedSessions = habitSessionFollowerRepository.queryGetParticipateSessionNumber(follower, habitId, sessionIds);
                participatedSessionsSum += participatedSessions;
            }
            // 종합 참여율
            participateRate = Math.round ((float) participatedSessionsSum / sessionIds.size() / followers.size() * 100);
        }
        return participateRate;
    }

    public double starRate(Long habitId, List<User> followers,
                        HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository){
        double starRate = 0.0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 해당 습관의 종료된 세션 목록
            List<Long> sessionIds = habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId);

            // 유저들의 참여한 횟수
            long starRateSum = 0l;
            for (User follower : followers){
                // 유저가 참여한 세션의 만족도
                List<Long> followerStarRates = habitSessionFollowerRepository.queryGetStarRate(follower,habitId,sessionIds);
                for(Long followerStarRate : followerStarRates){
                    starRateSum += followerStarRate;
                }
            }
            // 소수점 첫째 자리까지 출력
            starRate = Math.round((float) starRateSum / sessionIds.size() / followers.size() *100)/ 100.0  ;
        }
        return starRate;
    }
}
