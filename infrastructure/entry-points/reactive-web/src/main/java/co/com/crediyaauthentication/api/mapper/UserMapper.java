package co.com.crediyaauthentication.api.mapper;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {

   UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

   User toDomain(UserDto dto);

   UserDto toResponse(User user);
}
