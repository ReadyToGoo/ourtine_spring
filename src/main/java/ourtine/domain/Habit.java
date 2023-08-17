package ourtine.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.HabitStatus;
import ourtine.domain.enums.Status;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.mapping.HabitDays;
import ourtine.domain.mapping.HabitFollowers;
import ourtine.domain.mapping.HabitHashtag;
import software.amazon.ion.Decimal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@DynamicInsert
public class Habit extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Long categoryId;

    @OneToMany( mappedBy = "habit")
    private List<HabitFollowers> habitFollowersList = new ArrayList<>();

    @OneToMany( mappedBy = "habit")
    private List<HabitDays> days = new ArrayList<>();

    @OneToMany( mappedBy = "habit")
    private List<HabitHashtag> hashtags = new ArrayList<>();

    private Long followerCount = 1l;

    @Column(nullable = false)
    private Long followerLimit;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer participateRate = 0 ;

    @Column(nullable = false, precision =2, scale = 1)
    private BigDecimal starRate = BigDecimal.valueOf(0.0);

    // 공개/비공개 여부
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private HabitStatus habitStatus;

    // 활성 / 비활성 여부
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;


    public Habit(User host, String title, String detail, String imageUrl, Long categoryId, /*Long followerCount, */Long followerLimit,
                 LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, HabitStatus habitStatus){
        this.host = host;
        this.title = title;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.categoryId =  categoryId;
        this.followerLimit = followerLimit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate  =startDate;
        this.endDate = endDate;
        this.habitStatus = habitStatus;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl=imageUrl;
    }

    public void setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void updateParticipateRate(Integer participateRate){this.participateRate = participateRate;}

    public void updateStarRate(Decimal starRate){this.starRate=starRate;}
}
