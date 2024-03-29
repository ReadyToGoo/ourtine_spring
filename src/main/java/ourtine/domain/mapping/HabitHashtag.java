package ourtine.domain.mapping;

import lombok.Getter;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
import ourtine.domain.common.BaseEntity;
import javax.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class HabitHashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id",nullable = false)
    private Hashtag hashtag;

    @Builder
    public HabitHashtag(Habit habit, Hashtag hashtag){
        this.habit = habit;
        this.hashtag = hashtag;
    }
}
