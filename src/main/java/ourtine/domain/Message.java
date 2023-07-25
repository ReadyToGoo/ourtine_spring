package ourtine.domain;

import ourtine.domain.common.BaseEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ourtine.domain.enums.MessageType;
import ourtine.domain.enums.Status;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private User receiver;
    @Column(nullable = true)
    private String contents;

//    public Message(User sender, User receiver, MessageType messageType) {
//        this.sender=sender;
//        this.receiver=receiver;
//        this.messageType = messageType;
//    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
