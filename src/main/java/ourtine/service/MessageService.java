package ourtine.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ourtine.domain.Habit;
import ourtine.domain.NewMessage;
import ourtine.domain.OldMessage;
import ourtine.domain.User;
import ourtine.web.dto.response.HabitInvitationPostResponseDto;
import ourtine.web.dto.response.MessageResponseDto;

import java.util.List;

public interface MessageService {

    public void createNewMessage(NewMessage newMessage);

    public void newToOld(NewMessage newMessage);

    public Slice<MessageResponseDto> getMessages(User user, Pageable pageable);

    public void newFollowMessage(User sender, User receiver);

    public HabitInvitationPostResponseDto newHabitInviteMessage(User sender, List<Long> receiverIds, Habit habit);

}
