package ourtine.domain;

import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.SessionStatus;
import ourtine.domain.enums.Status;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SessionStatus status = SessionStatus.INACTIVE; // 자동 생성 시 INACTIVE

    @Builder
    public HabitSession(Habit habit, Date date){
        this.habit = habit;
        this.date = date;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
