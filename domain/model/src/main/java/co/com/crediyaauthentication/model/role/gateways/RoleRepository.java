package co.com.crediyaauthentication.model.role.gateways;

import co.com.crediyaauthentication.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById(Long id);
}
