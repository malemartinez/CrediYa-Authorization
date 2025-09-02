package co.com.crediyaauthentication.model.user;

import co.com.crediyaauthentication.model.Exceptions.ValidationException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class UserValidator {


    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void validate(User user) {
        Set<String> errors = new HashSet<>();

        validateName(user.getName(), errors);
        validateLastname(user.getLastname(), errors);
        validateSalary(user.getBaseSalary(), errors);
        validateEmail(user.getEmail(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateName(String name, Set<String> errors) {
        if (name == null || name.isBlank()) {
            errors.add("El nombre no puede estar vacío");
        }
    }

    private void validateLastname(String lastname, Set<String> errors) {
        if (lastname == null || lastname.isBlank()) {
            errors.add("El apellido no puede estar vacío");
        }
    }

    private void validateSalary(Double salary, Set<String> errors) {
        if (salary == null) {
            errors.add("El salario no puede estar vacío");
        } else if (salary <= 0 || salary > 15000000) {
            errors.add("El salario debe estar entre 0 y 15.000.000");
        }
    }

    private void validateEmail(String value, Set<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add("El correo no puede estar vacío");
        } else if (!EMAIL_REGEX.matcher(value).matches()) {
            errors.add("Formato de correo inválido");
        }
    }
}
