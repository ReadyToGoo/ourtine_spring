package ourtine.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.enums.Status;
import ourtine.domain.mapping.UserMvp;
import ourtine.repository.*;
import ourtine.util.CalculatorClass;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class VoteTasklet implements Tasklet, StepExecutionListener {

    private final UserMvpRepository userMvpRepository;

    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private final UserRepository userRepository;
    private CalculatorClass calculatorClass;
    private List<HabitSession> sessions;


    @Autowired
    public VoteTasklet(UserMvpRepository userMvpRepository, HabitFollowersRepository habitFollowersRepository, HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository, UserRepository userRepository) {
        this.userMvpRepository = userMvpRepository;
        this.habitFollowersRepository = habitFollowersRepository;
        this.habitSessionRepository = habitSessionRepository;
        this.habitSessionFollowerRepository = habitSessionFollowerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    // 습관 시간 종료 후 투표 집계
    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)  {
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(1);
        sessions = habitSessionRepository.queryFindActiveSession(time);
        // 투표 진행
        if (!sessions.isEmpty()) {
            sessions.forEach(session -> {
                List<User> followers = habitFollowersRepository.queryFindHabitFollowerIds(session.getHabit());
                List<Long> votes = habitSessionFollowerRepository.queryGetHabitSessionVotes(session.getId());

                if (!votes.isEmpty()) {
                    // 표가 1표라면
                    if (votes.size()==1){
                        User user = userRepository.findById(votes.get(0)).orElseThrow();
                        UserMvp userMvp = UserMvp.builder().habitSession(session).user(user).build();
                        userMvpRepository.save(userMvp);
                    }
                    else {
                        HashMap<Long, Long> resultMap = new HashMap<>(); // ( 유저 아이디, 득표 수)
                        long result = 0L; // 최다 득표수
                        for (int i = 0; i < followers.size(); i++) {
                            long voteNum = Collections.frequency(votes, followers.get(i).getId());
                            resultMap.put(followers.get(i).getId(), voteNum);
                            if (result <= voteNum) {
                                result = voteNum;
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
                // 세션 비활성화
                session.setStatus(Status.INACTIVE);
            });
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    @Transactional
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!sessions.isEmpty()) {
            sessions.forEach(session -> {
                // 유저 참여도 업데이트
                List<User> followers = habitFollowersRepository.queryFindHabitFollowerIds(session.getHabit());
                for(User follower : followers){
                    follower.updateParticipationRate(calculatorClass.myParticipationRate(follower,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository));
                }
                // 습관 참여도 업데이트
                session.getHabit().updateParticipateRate(calculatorClass.habitParticipateRate(session.getHabit().getId(),
                        followers,habitSessionRepository,habitSessionFollowerRepository,habitFollowersRepository));
                    });
        }
        return ExitStatus.COMPLETED;
    }
}
