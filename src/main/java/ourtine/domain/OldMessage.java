package ourtine.domain;

import javax.persistence.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Old")
public class OldMessage extends Message {

    public OldMessage(NewMessage newMessage) {
        super(newMessage.getMessageType(), newMessage.getSender(), newMessage.getReceiver(), newMessage.getContents(), newMessage.getHabitId());
    }
}
