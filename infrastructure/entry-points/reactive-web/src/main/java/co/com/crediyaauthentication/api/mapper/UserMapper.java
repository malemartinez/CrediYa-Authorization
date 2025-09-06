package co.com.crediyaauthentication.api.mapper;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {

   UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIds")
   User toDomain(UserDto dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleEntitiesToIds")
   UserDto toResponse(User user);

    @Named("mapRoleIds")
    default Set<Role> mapRoleIds(List<Long> roleIds) {
        if (roleIds == null) return new HashSet<>();
        return roleIds.stream()
                .map(id -> new Role(id, null))
                .collect(Collectors.toSet());
    }

    @Named("mapRoleEntitiesToIds")
    default List<Long> mapRoleEntitiesToIds(Set<Role> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Role::getId)
                .toList();
    }
}
