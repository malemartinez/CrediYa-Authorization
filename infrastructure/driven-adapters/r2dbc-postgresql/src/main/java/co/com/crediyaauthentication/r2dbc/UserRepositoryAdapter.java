package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.auth.gateways.AuthRepository;
import co.com.crediyaauthentication.model.helpers.PasswordEncoderPort;
import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import co.com.crediyaauthentication.r2dbc.entity.RoleEntity;
import co.com.crediyaauthentication.r2dbc.entity.UserEntity;
import co.com.crediyaauthentication.r2dbc.entity.UserRoleEntity;
import co.com.crediyaauthentication.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediyaauthentication.r2dbc.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
> implements UserRepository , AuthRepository {

    private final UserRoleReactiveRepository userRoleRepository;
    private final RoleReactiveRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoderPort passwordEncoderPort;

    public UserRepositoryAdapter(UserReactiveRepository repository,
                                 ObjectMapper mapper, UserRoleReactiveRepository userRoleRepository,
                                 RoleReactiveRepository roleRepository, JwtService jwtService, PasswordEncoderPort passwordEncoderPort) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Mono<User> save(User user) {
        log.trace("[UserRepositoryAdapter] Iniciando guardado del usuario: {}", user);

        return repository.save(mapper.map(user,UserEntity.class))
                .flatMap(userEntity -> saveRoles(userEntity, user))
                .doOnSuccess(u -> log.trace("[UserRepositoryAdapter]  Usuario guardado con roles exitosamente: {}", u))
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
                .flatMap(this::findRolesToUser)
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
                .flatMap(this::findRolesToUser)//TODO: MODIFICAR EL MAPPER PARA DEVOLVER LA CONTRASEÑA CON ENCODER
                .doOnSuccess(u -> {
                    if (u != null) {
                        log.trace(" [UserRepositoryAdapter] Usuario encontrado con identificacion {}: {}", documentIdentification, u);
                    } else {
                        log.trace("[UserRepositoryAdapter] No se encontró usuario con identificacion: {}", documentIdentification);
                    }
                })
                .doOnError(e -> log.error("[UserRepositoryAdapter] Error al buscar usuario por identificacion {}: {}", documentIdentification, e.getMessage(), e));

    }


//    public Mono<Token> login(LogIn dto) {
//        return repository.findByEmail(dto.getEmail())
//                .filter(userDocument -> passwordEncoderPort.matches(dto.getPassword(), userDocument.getPassword()))
//                .flatMap(userEntity ->
//                        userRoleRepository.findRoleIdsByUserId(userEntity.getId())
//                                .flatMap(roleRepository::findById)
//                                .map(RoleEntity::getName)
//                                .collectList()
//                                .map(roles -> new SecurityUser(userEntity, roles))
//                .map(userDocument -> new Token(jwtService.generateToken(userDocument)))
//                .switchIfEmpty(Mono.error(new BusinessException("Credenciales incorrectos"))));
//    }

    public Flux<Long> getRoleIdsByUserId(User user){
        UserEntity userEntity = mapper.map(user, UserEntity.class);
       return userRoleRepository.findRoleIdsByUserId(userEntity.getId());
    }

    private Mono<User> saveRoles(UserEntity u, User user){
        Long userId = u.getId();

        return Flux.fromIterable(user.getRoles())
                .flatMap(role -> userRoleRepository.save(new UserRoleEntity(userId, role.getId())))
                .thenMany(Flux.fromIterable(user.getRoles()))
                .collectList()
                .map(roles -> {
                    User domainUser = mapper.map(u, User.class);
                    domainUser.setRoles(new HashSet<>(roles));
                    return domainUser;
                });
    }

    private Mono<User> findRolesToUser(UserEntity userEntity){
        return userRoleRepository.findByUserId(userEntity.getId())
                .map(UserRoleEntity::getRoleId)
                .collectList()
                .flatMap(roleIds -> roleRepository.findAllByIdIn(roleIds).collectList())
                .map(roleEntities -> mapRolesToUser(roleEntities, userEntity));
    }

    private User mapRolesToUser(List<RoleEntity> roleEntities, UserEntity userEntity){
        User domainUser = mapper.map(userEntity, User.class);

        // Mapeamos los roles con nombre
        List<Role> roles = roleEntities.stream()
                .map(r -> new Role(r.getId(), r.getName()))
                .toList();
        domainUser.setRoles(new HashSet<>(roles));

        return domainUser;
    }

}
