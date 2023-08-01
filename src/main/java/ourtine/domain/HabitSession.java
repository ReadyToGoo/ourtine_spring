package ourtine.domain;

import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    private Status status = Status.ACTIVE;

    @Builder
    public HabitSession(Habit habit, Date date){
        this.habit = habit;
        this.date = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
