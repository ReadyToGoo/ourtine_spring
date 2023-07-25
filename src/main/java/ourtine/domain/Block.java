package ourtine.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "blocker_id",nullable = false)
    private User blocker;
    @ManyToOne
    @JoinColumn(name = "blocked_id",nullable = false)
    private User blocked;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
