package co.com.crediyaauthentication.usecase.user;

import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.model.user.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.user.Exceptions.ValidationException;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.UserValidator;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;

    public Mono<User> saveUser(User user) {

        return Mono.fromCallable(() -> {
                    userValidator.validate(user);
                    return user;
                })
                .flatMap(u -> userRepository.findByEmail(u.getEmail())
                        .flatMap(existing -> Mono.<User>error(new BusinessException("El email ya está registrado")))
                        .switchIfEmpty(Mono.just(u))
                )
                .flatMap(u -> roleRepository.findById(u.getIdRole())
                        .switchIfEmpty(Mono.error(new BusinessException("El rol no existe")))
                        .map(role -> u)
                )
                .flatMap(userRepository::save)
                .onErrorResume(ValidationException.class, Mono::error);
    }


}
