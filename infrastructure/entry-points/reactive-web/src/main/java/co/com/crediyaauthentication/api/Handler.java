package co.com.crediyaauthentication.api;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.api.mapper.UserMapper;
import co.com.crediyaauthentication.model.user.gateways.UserCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

  private  final UserCasePort useCase;
  private  final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDto.class)
                .map(userMapper::toDomain)
                .flatMap(useCase::saveUser)
                .map(userMapper::toResponse)
                .flatMap(savedUserDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUserDTO));
    }
    public Mono<ServerResponse> getUserByDocument(ServerRequest serverRequest) {
        String documentNumber = serverRequest.pathVariable("documentNumber");

        return useCase.getUserByDocument(documentNumber)
                .doOnNext(u -> log.info("[UserHandler] Recibida petición de búsqueda de usuario, userDocument={}", documentNumber))
                .map(userMapper::toResponse)
                .flatMap(userResponse ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
