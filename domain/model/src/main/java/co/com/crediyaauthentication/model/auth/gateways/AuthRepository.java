package co.com.crediyaauthentication.model.auth.gateways;

import co.com.crediyaauthentication.model.user.User;
import reactor.core.publisher.Flux;

public interface AuthRepository {
    Flux<Long> getRoleIdsByUserId(User user);
}
