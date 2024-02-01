package com.financing.app.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface UserMapper {
    User fromUserDTOtoUser(UserDTO user);

    UserDTO fromUserToUserDTO(User user);
}
