package ourtine.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.mapping.UserMvp;
import ourtine.repository.HabitFollowersRepository;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;
import ourtine.repository.UserMvpRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class VoteTasklet implements Tasklet {

    private final UserMvpRepository userMvpRepository;

    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;


    @Autowired
    public VoteTasklet(UserMvpRepository userMvpRepository, HabitFollowersRepository habitFollowersRepository, HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository) {
        this.userMvpRepository = userMvpRepository;
        this.habitFollowersRepository = habitFollowersRepository;
        this.habitSessionRepository = habitSessionRepository;
        this.habitSessionFollowerRepository = habitSessionFollowerRepository;
    }


    // 습관 시간 종료 후 투표 집계
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(1);
        List<HabitSession> sessions = habitSessionRepository.queryFindActiveSession(time);

        // 투표 진행
        if (sessions.size()>0) {
            sessions.forEach(session -> {
                List<User> followers = habitFollowersRepository.queryFindHabitFollowerIds(session.getHabit());
                List<Long> votes = habitSessionFollowerRepository.queryGetHabitSessionVotes(session.getId());

                if (votes.size()>0) {
                    if (votes.size()==1){
                        // TODO: 유저 코드
                        /*UserMvp userMvp = UserMvp.builder().habitSession(session).user().build();
                        userMvpRepository.save(userMvp);*/
                    }
                    else {
                        long[] voteNum = new long[followers.size()]; // 득표 수 저장
                        HashMap<Long, Long> resultMap = new HashMap<>(); // ( 유저 아이디, 득표 수)
                        long result = 0L; // 최다 득표수

                        for (int i = 0; i < followers.size(); i++) {
                            voteNum[i] = Collections.frequency(votes, followers.get(i).getId());
                            resultMap.put(followers.get(i).getId(), voteNum[i]);
                            if (result <= voteNum[i]) {
                                result = voteNum[i];
                            }
                        }

                        for (User follower : followers) {
                            if (resultMap.get(follower.getId()) == result) { // 최다 득표수와 유저의 득표수가 같다면 유저 아이디 저장
                                UserMvp userMvp = UserMvp.builder().habitSession(session).user(follower).build();
                                userMvpRepository.save(userMvp);
                            }
                        }
                    }
                }

            });
        }


        return RepeatStatus.FINISHED;
    }
}
