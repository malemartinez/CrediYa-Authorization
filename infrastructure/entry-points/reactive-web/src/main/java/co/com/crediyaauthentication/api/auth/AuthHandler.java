package co.com.crediyaauthentication.api.auth;

import co.com.crediyaauthentication.api.mapper.AuthMapper;
import co.com.crediyaauthentication.usecase.user.auth.AuthUseCase;
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
public class AuthHandler {

    private final AuthUseCase useCase;
    private final AuthMapper authMapper;

    public Mono<ServerResponse> logInUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LogInDTO.class)
                .map(authMapper::toDomain)
                .flatMap(useCase::logIn)
                .map(authMapper::toResponse)
                .flatMap(logInToken -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(logInToken));
    }
}
