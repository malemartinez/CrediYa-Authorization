package co.com.crediyaauthentication.api.mapper;

import co.com.crediyaauthentication.api.auth.LogInDTO;
import co.com.crediyaauthentication.api.auth.TokenResponse;
import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    LogIn toDomain(LogInDTO dto);

    TokenResponse toResponse(Token token);
}
