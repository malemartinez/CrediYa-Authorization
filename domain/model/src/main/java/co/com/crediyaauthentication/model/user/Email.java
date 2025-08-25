package co.com.crediyaauthentication.model.user;

import co.com.crediyaauthentication.model.user.Exceptions.ValidationException;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("El correo no puede estar vacío");
        }
        if (!EMAIL_REGEX.matcher(value).matches()) {
            throw new ValidationException("Formato de correo inválido");
        }
        this.value = value.toLowerCase();
    }

    public String getValue() {
        return value;
    }
}
