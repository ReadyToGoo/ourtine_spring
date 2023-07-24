package ourtine.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.common.BaseEntity;
import ourtine.domain.enums.Status;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id",nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id",nullable = false)
    private User user2;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
