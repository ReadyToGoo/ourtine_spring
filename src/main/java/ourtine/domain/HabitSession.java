package ourtine.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

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

    @ManyToOne
    @JoinColumn(name = "mvp_id")
    private User mvp;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
