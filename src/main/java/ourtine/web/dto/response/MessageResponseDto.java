package ourtine.web.dto.response;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import ourtine.domain.NewMessage;
import ourtine.domain.enums.MessageType;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
public class MessageResponseDto {
    private String messageType;
    private String sender;
    private String contents;
    private String date;

    public MessageResponseDto(NewMessage newMessage) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM월 dd일");
        this.messageType = newMessage.getMessageType().getDescription();
        this.sender = newMessage.getSender().getNickname();
        this.contents = newMessage.getContents();
        this.date = (newMessage.getCreatedAt()).format(dateTimeFormatter);
    }
}
