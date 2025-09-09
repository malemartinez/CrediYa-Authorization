package co.com.crediyaauthentication.r2dbc.config;

import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.gateways.UserCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TransactionalUserUseCase implements UserCasePort {

    private final UserCasePort delegate;
    private final TransactionalOperator operator;
    @Override
    public Mono<User> saveUser(User user) {
        return delegate.saveUser(user)
                .as(operator::transactional);
    }

    @Override
    public Mono<User> getUserByDocument(String documentNumber) {
        return delegate.getUserByDocument(documentNumber)
                .as(operator::transactional);
    }

    @Override
    public Mono<Token> logIn(LogIn logIn) {
        return delegate.logIn(logIn)
                .as(operator::transactional);
    }
}
