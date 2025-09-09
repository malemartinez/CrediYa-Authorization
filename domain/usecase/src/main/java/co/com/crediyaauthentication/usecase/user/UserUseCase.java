package co.com.crediyaauthentication.usecase.user;

import co.com.crediyaauthentication.model.helpers.PasswordEncoderPort;
import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.Exceptions.ValidationException;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.UserValidator;
import co.com.crediyaauthentication.model.user.gateways.UserCasePort;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;


@RequiredArgsConstructor
public class UserUseCase implements UserCasePort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public Mono<User> saveUser(User user) {
        return Mono.fromCallable(() -> {

                    userValidator.validate(user);

                    if (user.getPassword() == null || user.getPassword().isBlank()) {
                        user.setPassword(user.getDocumentIdentification());
                    }
                    user.setPassword(passwordEncoder.encode(user.getPassword()));

                    return user;
                })
                .flatMap(this::validateEmail)
                .flatMap(this::ValidateRoles)
                .flatMap(userRepository::save)
                .onErrorResume(ValidationException.class, Mono::error);
    }

    @Override
    public Mono<User> getUserByDocument(String documentNumber) {
        return userRepository.findByDocumentIdentification(documentNumber);
    }

    @Override
    public Mono<Token> logIn(LogIn logIn) {
        return userRepository.login(logIn);
    }

    private Mono<User> validateEmail(User u){
        return userRepository.findByEmail(u.getEmail())
                .flatMap(existing -> Mono.<User>error(new BusinessException("El email ya está registrado")))
                .switchIfEmpty(Mono.just(u));
    }

    private Mono<User> ValidateRoles(User u){

        List<Long> roleIds = u.getRoles().stream().map(Role::getId).toList();
        return roleRepository.findAllById(roleIds)
                .collectList()
                .flatMap(foundRoles -> {
                    if (foundRoles.size() != roleIds.size()) {
                        return Mono.error(new BusinessException("Uno o más roles no existen"));
                    }
                    return Mono.just(u);
                });
    }


}
