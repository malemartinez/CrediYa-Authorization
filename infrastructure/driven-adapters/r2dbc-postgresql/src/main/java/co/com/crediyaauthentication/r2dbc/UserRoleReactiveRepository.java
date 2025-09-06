package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.r2dbc.entity.UserRoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRoleReactiveRepository extends ReactiveCrudRepository<UserRoleEntity, Long>, ReactiveQueryByExampleExecutor<UserRoleEntity> {

    Flux<UserRoleEntity> findByUserId(Long userId);
}
