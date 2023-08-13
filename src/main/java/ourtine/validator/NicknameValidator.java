package ourtine.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ourtine.repository.UserRepository;
import ourtine.web.dto.request.NicknameChangeRequestDto;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameChangeRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameChangeRequestDto nicknameChangeRequestDto = (NicknameChangeRequestDto) target;
        if (userRepository.existsByNickname(nicknameChangeRequestDto.getNickname())) {
            errors.rejectValue("nickname", "wrong.value", "이미 사용중인 닉네임입니다.");
        }
    }
}
