package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.NewMessage;
import ourtine.domain.OldMessage;
import ourtine.domain.User;
import ourtine.repository.NewMessageRepository;
import ourtine.repository.OldMessageRepository;
import ourtine.service.MessageService;
import ourtine.web.dto.response.MessageResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final NewMessageRepository newMessageRepository;
    private final OldMessageRepository oldMessageRepository;
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

}