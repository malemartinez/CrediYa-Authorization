package co.com.crediyaauthentication.api.mapper;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {

   UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

   // DTO → Dominio
//   @Mapping(target = "name", expression = "java(new Name(dto.getName()))")
//   @Mapping(target = "lastname", expression = "java(new Lastname(dto.getLastname()))")
   //@Mapping(target = "email", expression = "java(new co.com.crediyaauthentication.model.user.Email(dto.email()))")
//   @Mapping(target = "baseSalary", expression = "java(new Salary(dto.getBaseSalary()))")
//   @Mapping(target = "phone", expression = "java(new Phone(dto.getPhone()))")
//   @Mapping(target = "documentIdentification", expression = "java(new DocumentIdentification(dto.getDocumentIdentification()))")
   User toDomain(UserDto dto);

   // Dominio → DTO
//   @Mapping(target = "name", expression = "java(user.getName().getValue())")
//   @Mapping(target = "lastname", expression = "java(user.getLastname().getValue())")
   //@Mapping(target = "email", expression = "java(user.getEmail().getValue())")
//   @Mapping(target = "baseSalary", expression = "java(user.getBaseSalary().getValue())")
//   @Mapping(target = "phone", expression = "java(user.getPhone().getValue())")
//   @Mapping(target = "documentIdentification", expression = "java(user.getDocumentIdentification().getValue())")
   UserDto toResponse(User user);
}
