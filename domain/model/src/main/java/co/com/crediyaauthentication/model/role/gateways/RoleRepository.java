package co.com.crediyaauthentication.model.role.gateways;

import co.com.crediyaauthentication.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoleRepository {
    Mono<Role> findById(Long id);
    Flux<Role> findAllById(List<Long> ids);
}
