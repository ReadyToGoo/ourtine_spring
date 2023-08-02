package ourtine.domain;

import javax.persistence.*;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ourtine.domain.enums.MessageType;

@Entity

public class NewMessage extends Message {
    @Builder
    public NewMessage(MessageType messageType, User sender, User receiver, String contents){
        super(messageType, sender, receiver, contents);
    }
}
