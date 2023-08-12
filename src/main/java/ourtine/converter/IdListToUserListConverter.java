package ourtine.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ourtine.domain.User;
import ourtine.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IdListToUserListConverter {

    private final UserService userService;

    public List<User> idToUser(List<Long> userIds) {
        List<User> userList = new ArrayList<>();
        for (Long id : userIds) {
            userList.add(userService.findById(id));
        }
        return userList;
    }

}
