package co.com.crediyaauthentication.api.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, Object message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value()
        );

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);

            var response = exchange.getResponse();
            response.setStatusCode(status);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getHeaders().setCacheControl("no-store"); // opcional, típico en errores de seguridad

            var buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer))
                    .doOnError(e -> DataBufferUtils.release(buffer));

        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
