package ourtine.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.HabitStatus;
import ourtine.domain.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Habit extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    private String detail;

    private String imageUrl;

    private Long categoryId;

    private Long followerCount;
    @Column(nullable = false)

    private Long followerLimit;

    // 습관 시작 시간
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    // 공개/비공개 여부
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private HabitStatus habitStatus;

    // 활성/비활성 여부
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

}
