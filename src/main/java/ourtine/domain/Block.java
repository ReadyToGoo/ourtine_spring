package ourtine.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id",nullable = false)
    private User blocker;
     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id",nullable = false)
    private User blocked;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;
}
