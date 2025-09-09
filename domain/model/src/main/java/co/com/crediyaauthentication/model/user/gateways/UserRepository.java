package co.com.crediyaauthentication.model.user.gateways;

import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
    Mono<User> findByDocumentIdentification(String documentIdentification);
    Mono<Token> login(LogIn dto);
}
