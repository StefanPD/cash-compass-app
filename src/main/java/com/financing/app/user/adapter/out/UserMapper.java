package com.financing.app.user.adapter.out;

import com.financing.app.user.application.domain.model.UserDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface UserMapper {
    User fromUserDTOtoUser(UserDTO userDto);

    UserDTO fromUserToUserDTO(User user);
}
