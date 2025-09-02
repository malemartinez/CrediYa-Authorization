package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import co.com.crediyaauthentication.r2dbc.entity.UserEntity;
import co.com.crediyaauthentication.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Slf4j
@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
> implements UserRepository{

    public UserRepositoryAdapter(UserReactiveRepository repository,
                                 ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> save(User user) {
        log.trace("[UserRepositoryAdapter] Iniciando guardado del usuario: {}", user);
        return super.save(user)
                .doOnSuccess(u -> log.trace("[UserRepositoryAdapter] Usuario guardado exitosamente: {}", u))
                .doOnError(e -> log.error("[UserRepositoryAdapter] Error al guardar usuario: {}", e.getMessage(), e))
                .onErrorMap(org.springframework.dao.DataIntegrityViolationException.class,
                        ex ->  {
                            log.warn("[UserRepositoryAdapter] Violación de integridad al guardar usuario [{}]: {}", user, ex.getMessage());
                            return new BusinessException("Violacion de datos para guardado del usuario");
                        } );

    }

    @Override
    public Mono<User> findByEmail(String email) {
        log.trace("[UserRepositoryAdapter] Buscando usuario por email: {}", email);
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class))
                .doOnSuccess(u -> {
                    if (u != null) {
                        log.trace("[UserRepositoryAdapter] Usuario encontrado por email {}: {}", email, u);
                    } else {
                        log.trace("[UserRepositoryAdapter] No se encontró usuario con email: {}", email);
                    }
                })
                .doOnError(e -> log.error("[UserRepositoryAdapter] Error al buscar usuario por email {}: {}", email, e.getMessage(), e));

    }

    @Override
    public Mono<User> findByDocumentIdentification(String documentIdentification) {
        log.trace("[UserRepositoryAdapter] Buscando usuario por identificacion: {}", documentIdentification);
        return repository.findByDocumentIdentification(documentIdentification)
                .map(entity -> mapper.map(entity, User.class))
                .doOnSuccess(u -> {
                    if (u != null) {
                        log.trace(" [UserRepositoryAdapter] Usuario encontrado con identificacion {}: {}", documentIdentification, u);
                    } else {
                        log.trace("[UserRepositoryAdapter] No se encontró usuario con identificacion: {}", documentIdentification);
                    }
                })
                .doOnError(e -> log.error("[UserRepositoryAdapter] Error al buscar usuario por identificacion {}: {}", documentIdentification, e.getMessage(), e));

    }
}
