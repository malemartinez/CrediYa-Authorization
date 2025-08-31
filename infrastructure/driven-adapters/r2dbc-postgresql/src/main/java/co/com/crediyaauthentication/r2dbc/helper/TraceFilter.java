package co.com.crediyaauthentication.r2dbc.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TraceFilter implements WebFilter {

    private static final String TRACE_ID = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String traceId = UUID.randomUUID().toString();

        // Añadimos el traceId al contexto Reactor
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(TRACE_ID, traceId));
    }
}
