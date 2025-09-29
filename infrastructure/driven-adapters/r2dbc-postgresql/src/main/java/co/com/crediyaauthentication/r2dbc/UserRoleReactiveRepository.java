package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.r2dbc.entity.UserRoleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRoleReactiveRepository extends ReactiveCrudRepository<UserRoleEntity, Long>, ReactiveQueryByExampleExecutor<UserRoleEntity> {

    Flux<UserRoleEntity> findByUserId(Long userId);

    @Query("SELECT role_id FROM user_roles WHERE user_id = :userId")
    Flux<Long> findRoleIdsByUserId(Long userId);
}
