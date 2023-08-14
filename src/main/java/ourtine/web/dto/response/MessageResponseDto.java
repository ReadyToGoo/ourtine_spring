package ourtine.web.dto.response;

import lombok.Getter;
import ourtine.domain.NewMessage;
import java.time.format.DateTimeFormatter;

@Getter
public class MessageResponseDto {
    private String messageType;
    private String sender;
    private String contents;
    private String date;
    private Long habitId = null;

    public MessageResponseDto(NewMessage newMessage) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM월 dd일");
        this.messageType = newMessage.getMessageType().getDescription();
        this.sender = newMessage.getSender().getNickname();
        this.contents = newMessage.getContents();
        this.date = (newMessage.getCreatedAt()).format(dateTimeFormatter);
        this.habitId = newMessage.getHabitId();
    }
}
