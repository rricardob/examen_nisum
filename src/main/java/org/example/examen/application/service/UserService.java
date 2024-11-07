package org.example.examen.application.service;

import lombok.RequiredArgsConstructor;
import org.example.examen.application.mapper.PhoneDTOMapper;
import org.example.examen.application.port.input.UserUseCase;
import org.example.examen.application.port.output.PhonePersistencePort;
import org.example.examen.common.exception.CustomerDuplicationViolationException;
import org.example.examen.common.exception.messages.ExceptionMessages;
import org.example.examen.domain.model.Phone;
import org.example.examen.domain.model.User;
import org.example.examen.domain.model.dto.UserDTO;
import org.example.examen.application.port.output.UserPersistencePort;

import java.util.UUID;

@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PhonePersistencePort phonePersistencePort;
    private final PhoneDTOMapper phoneDTOMapper;

    @Override
    public UserDTO createUser(User user) {
        if (this.userPersistencePort.existsUserByEmail(user.getEmail())) {
            throw new CustomerDuplicationViolationException(ExceptionMessages.USER_EMAIL_EXIST_ERROR_MESSAGE);
        }

        User userCreated = this.userPersistencePort.create(user);
        if (!user.getPhones().isEmpty()) {
            user.getPhones().forEach(phone ->{
                Phone phoneModel = this.phoneDTOMapper.toModel(phone);
                phoneModel.setUserId(userCreated.getId());
                this.phonePersistencePort.create(phoneModel);
            });
        }

        return UserDTO.builder()
                .id(UUID.randomUUID())
                .created(userCreated.getCreated())
                .modified(userCreated.getModified())
                .lastLogin(userCreated.getLastLogin())
                .token(userCreated.getToken())
                .active(userCreated.isActive())
                .build();
    }
}
