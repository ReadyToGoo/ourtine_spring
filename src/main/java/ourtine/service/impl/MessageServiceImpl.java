package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.converter.IdListToUserListConverter;
import ourtine.converter.MessageContentsConverter;
import ourtine.domain.Habit;
import ourtine.domain.NewMessage;
import ourtine.domain.OldMessage;
import ourtine.domain.User;
import ourtine.domain.enums.MessageType;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;
import ourtine.repository.NewMessageRepository;
import ourtine.repository.OldMessageRepository;
import ourtine.service.MessageService;
import ourtine.web.dto.response.HabitInvitationPostResponseDto;
import ourtine.web.dto.response.MessageResponseDto;

import java.util.List;
import java.util.stream.Collectors;

import static ourtine.exception.enums.ResponseMessage.WRONG_HABIT_INVITE;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final NewMessageRepository newMessageRepository;
    private final OldMessageRepository oldMessageRepository;
    private final MessageContentsConverter messageContentsConverter;
    private final IdListToUserListConverter idListToUserListConverter;

    private static final Long messageCount = 30L;

    @Override
    public void createNewMessage(NewMessage newMessage) {
        newMessageRepository.save(newMessage);
    }

    @Override
    public void newToOld(NewMessage newMessage) {
        OldMessage oldMessage = new OldMessage(newMessage);
        newMessageRepository.deleteById(newMessage.getId());
        oldMessageRepository.save(oldMessage);
    }

    @Override
    public Slice<MessageResponseDto> getMessages(User user, Pageable pageable) {
        Slice<NewMessage> newMessages = newMessageRepository.findByReceiver(user, pageable);
        List<MessageResponseDto> messageResponseDtoList = newMessages.getContent().stream()
                .map(m-> new MessageResponseDto(m))
                .collect(Collectors.toList());
//        if (messageResponseDtoList.size() < messageCount) {
//
//        }
        return new SliceImpl<>(messageResponseDtoList, newMessages.getPageable(), newMessages.hasNext());
    }


    @Override
    public void newFollowMessage(User sender, User receiver) {
        createNewMessage(new NewMessage(MessageType.FOLLOW, sender, receiver, messageContentsConverter.createContents(sender),null));
    }
    @Override
    @Transactional
    public HabitInvitationPostResponseDto newHabitInviteMessage(User sender, List<Long> receiverIds, Habit habit) {
        if (receiverIds.size() <= habit.getFollowerLimit() - 1) {
            List<User> userList = idListToUserListConverter.idToUser(receiverIds);
            for (User receiver : userList) {
                createNewMessage(new NewMessage(MessageType.HABIT_INVITE, sender, receiver, messageContentsConverter.createContents(sender, habit), habit.getId()));
            }
            return new HabitInvitationPostResponseDto(habit.getId());
        }
        // 초대한 인원 수가 습관 수용 인원보다 크다면
        else throw new BusinessException(WRONG_HABIT_INVITE);
    }
}