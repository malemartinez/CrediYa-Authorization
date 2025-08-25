package co.com.crediyaauthentication.usecase.user;

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
    private final UserValidator userValidator;

    public Mono<User> saveUser(User user) {

        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            return Mono.error(e);
        }

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new BusinessException("El email ya está registrado")))
                .switchIfEmpty(userRepository.save(user));
    }


}
