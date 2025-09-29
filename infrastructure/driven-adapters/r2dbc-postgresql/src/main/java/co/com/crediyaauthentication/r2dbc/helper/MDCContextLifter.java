package co.com.crediyaauthentication.r2dbc.helper;

import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

public class MDCContextLifter <T> implements CoreSubscriber<T> {
    private final CoreSubscriber<? super T> actual;

    public MDCContextLifter(CoreSubscriber<? super T> actual) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe(Subscription s) {
        actual.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        copyToMdc(actual.currentContext());
        actual.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        MDC.clear();
        actual.onError(t);
    }

    @Override
    public void onComplete() {
        MDC.clear();
        actual.onComplete();
    }

    @Override
    public Context currentContext() {
        return actual.currentContext();
    }

    private void copyToMdc(Context context) {
        context.<String>getOrEmpty("traceId")
                .ifPresent(traceId -> MDC.put("traceId", traceId));
    }
}
