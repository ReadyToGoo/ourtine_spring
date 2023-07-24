package ourtine.domain.mapping;

import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
import ourtine.domain.common.BaseEntity;
import javax.persistence.*;

public class HabitHashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id",nullable = false)
    private Habit habit;

    @OneToOne
    @JoinColumn(name = "hashtag_id",nullable = false)
    private Hashtag hashtag;
}
