package ourtine.util;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ourtine.domain.User;
import ourtine.repository.HabitFollowersRepository;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public final class CalculatorClass {

    public int myHabitParticipationRate(Long habitId, User user,
                                        HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                        HabitFollowersRepository habitFollowersRepository){
        int participateRate = 0;
        // 참여 날짜
        LocalDate joinDate = habitFollowersRepository.findByHabitIdAndFollowerId(habitId, user.getId()).get().getCreatedAt().toLocalDate();
        // 해당 습관의 종료된 세션 목록
        List<Long> sessionIds = habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId,java.sql.Date.valueOf(joinDate));
        // 참여한 날짜 이후로 진행 된 세션이 하나라도 있으면 참여율 산출
        if (!sessionIds.isEmpty()){
            // 유저가 세션에 참여한 횟수
            Long participatedSessions = habitSessionFollowerRepository.queryGetParticipateSessionNumber(user, habitId, sessionIds);
            // 내 참여율
            participateRate = Math.round((float) participatedSessions / sessionIds.size()) * 100;
        }
        return participateRate;
    }

    public int myParticipationRate(User user,
                                   HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                                   HabitFollowersRepository habitFollowersRepository){
        int participateRate = 0;
        List<Long> habitIds = habitFollowersRepository.queryFindMyHabitIds(user.getId(), Pageable.unpaged()).getContent();
        if (habitIds.size()>0){
            int participateRateSum = 0;
            for (Long habitId : habitIds) {
                // 내 습관 참여도 합
                participateRateSum += myHabitParticipationRate(habitId,user,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository);
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
                participateRateSum += myHabitParticipationRate(habitId,follower,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository);
            }
            participateRate = Math.round((float) participateRateSum / followers.size());
        }
        return participateRate;
    }

    public double habitStarRate(Long habitId,
                                 HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository){
        double starRate = 0.0;
        // 진행 된 세션이 하나라도 있으면 참여율 산출
        if (habitSessionRepository.queryFindEndSessionIdsByHabitId(habitId).size()>0){
            // 유저들의 참여한 횟수
            int starRateSum = 0;
            List<Integer> starRates = habitSessionFollowerRepository.queryGetStarRate(habitId);
            if (!starRates.isEmpty())
            {for (Integer s : starRates) {
                    starRateSum += s;}
                // 소수점 첫째 자리까지 출력
                starRate =  (double) Math.round((float) starRateSum / starRates.size() * 10) / 10;
            }
        }
        return starRate;
    }

    //userWeeklyLog 기간 파싱
    public String userWeeklyLogPeriod(String weeklyLog) {
        if (weeklyLog.isBlank()) return "empty";
        else {
            String dateString = weeklyLog.substring(1, 11);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate currentDate = LocalDate.parse(dateString, formatter);
            LocalDate.parse(dateString, formatter).atStartOfDay();
            LocalDate startDate = currentDate.minus(7, ChronoUnit.DAYS);
            return startDate.format(DateTimeFormatter.ofPattern("MM/dd")) + " ~ " + currentDate.format(DateTimeFormatter.ofPattern("MM/dd"));
        }
    }

    // userWeeklyLog 본문 파싱
    public String userWeeklyLogContents(String weeklyLog) {
        if (weeklyLog.isBlank()) return "empty";
        else return weeklyLog.substring(12);
    }
}
