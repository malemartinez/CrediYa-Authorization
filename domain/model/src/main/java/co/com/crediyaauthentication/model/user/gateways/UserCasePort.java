package co.com.crediyaauthentication.model.user.gateways;

import co.com.crediyaauthentication.model.user.User;
import reactor.core.publisher.Mono;

public interface UserCasePort {
    Mono<User> saveUser(User user);
}
