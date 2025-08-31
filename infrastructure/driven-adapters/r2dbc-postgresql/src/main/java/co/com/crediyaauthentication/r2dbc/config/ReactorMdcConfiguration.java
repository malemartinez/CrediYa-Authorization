package co.com.crediyaauthentication.r2dbc.config;

import co.com.crediyaauthentication.r2dbc.helper.MDCContextLifter;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

@Configuration
public class ReactorMdcConfiguration {

    @PostConstruct
    public void contextOperatorHook() {
        Hooks.onEachOperator("MDC_CONTEXT", Operators.lift((sc, sub) -> new MDCContextLifter<>(sub)));
    }
}
