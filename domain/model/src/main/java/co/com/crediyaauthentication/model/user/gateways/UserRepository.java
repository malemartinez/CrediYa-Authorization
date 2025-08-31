package co.com.crediyaauthentication.model.user.gateways;

import co.com.crediyaauthentication.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
    Mono<User> findByDocumentIdentification(String documentIdentification);
}
