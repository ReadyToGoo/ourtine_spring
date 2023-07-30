package ourtine.domain;

import lombok.*;
import ourtine.domain.common.BaseEntity;
import javax.persistence.*;

import ourtine.domain.enums.MessageType;
import ourtine.domain.enums.Status;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    public Message(MessageType messageType,User sender, User receiver, String contents){
        this.messageType =messageType;
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
    }
}
