package ourtine.domain.mapping;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@NoArgsConstructor
@Entity
public class HabitDays extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Day day;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Builder
    public HabitDays(Habit habit, Day day){
        this.habit = habit;
        this.day = day;
    }

}
