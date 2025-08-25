package co.com.crediyaauthentication.usecase.user;

import co.com.crediyaauthentication.model.user.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.user.Exceptions.ValidationException;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Mono<User> saveUser(User user) {

        validateUser(user);

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new BusinessException("El email ya está registrado")))
                .switchIfEmpty(userRepository.save(user));
    }

    private void validateUser(User user) {
        validateName(user.getName());
        validateLastname(user.getLastname());
        validateSalary(user.getBaseSalary());
        validateEmail(user.getEmail());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("El nombre no puede estar vacío");
        }
    }

    private void validateLastname(String lastname) {
        if (lastname == null || lastname.isBlank()) {
            throw new ValidationException("El apellido no puede estar vacío");
        }
    }

    private void validateSalary(Double salary) {
        if (salary == null) {
            throw new ValidationException("El salario no puede estar vacío");
        }
        if (salary <= 0 || salary > 15_000_000) {
            throw new ValidationException("El salario debe estar entre 0 y 15.000.000");
        }
    }

    private void validateEmail(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("El correo no puede estar vacío");
        }
        if (!EMAIL_REGEX.matcher(value).matches()) {
            throw new ValidationException("Formato de correo inválido");
        }

    }

}
