package co.com.crediyaauthentication.api.error;

import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.Exceptions.ValidationException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

        public GlobalErrorHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext) {
            super(errorAttributes, new WebProperties.Resources(), applicationContext);
            super.setMessageWriters(ServerCodecConfigurer.create().getWriters());
            super.setMessageReaders(ServerCodecConfigurer.create().getReaders());
        }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        Throwable error = getError(request);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Object messages = error.getMessage();

        log.error("Error procesando request {}: {}", request.path(), error.getMessage(), error);
        if (error instanceof ValidationException ve) {
            status = HttpStatus.BAD_REQUEST;
            messages = ve.getErrors();
        } else if (error instanceof BusinessException) {
            status = HttpStatus.CONFLICT;
        } else if (error instanceof JwtException
                || error instanceof IllegalArgumentException) {
            status = HttpStatus.UNAUTHORIZED;
            messages = "Token inválido, expirado o malformado";
        }

        ApiErrorResponse response = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                messages,
                request.path()
        );

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
