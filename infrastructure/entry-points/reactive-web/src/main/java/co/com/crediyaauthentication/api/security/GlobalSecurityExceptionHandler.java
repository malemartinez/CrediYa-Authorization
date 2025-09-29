package co.com.crediyaauthentication.api.security;

import co.com.crediyaauthentication.api.error.ErrorResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalSecurityExceptionHandler
        implements ServerAccessDeniedHandler, ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return ErrorResponseUtil.writeError(
                exchange,
                HttpStatus.UNAUTHORIZED,
                "Token inválido o ausente"
        );
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return ErrorResponseUtil.writeError(
                exchange,
                HttpStatus.FORBIDDEN,
                "No tienes permisos suficientes"
        );
    }
}

