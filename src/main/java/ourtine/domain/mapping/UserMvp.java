package ourtine.domain.mapping;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.HabitSession;
import ourtine.domain.User;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMvp extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id",nullable = false)
    private HabitSession habitSession;

    @ManyToOne
    @JoinColumn(name = "mvp_id",nullable = false)
    private User user;

    @Builder
    public UserMvp(HabitSession habitSession, User user){
        this.habitSession = habitSession;
        this.user = user;
    }

}
