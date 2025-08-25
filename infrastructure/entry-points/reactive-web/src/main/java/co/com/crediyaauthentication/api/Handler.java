package co.com.crediyaauthentication.api;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.api.mapper.UserMapper;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

  private  final UserUseCase useCase;
  private  final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDto.class)   // JSON → DTO
                .map(userMapper::toDomain)                     // DTO → Dominio
                .flatMap(useCase::saveUser)                    // Caso de uso devuelve User (dominio)
                .map(userMapper::toResponse)                   // Dominio → DTO de salida
                .flatMap(savedUserDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUserDTO));             // JSON final limpio
    }
}
