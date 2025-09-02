package co.com.crediyaauthentication.model.Exceptions;

import java.util.Set;

public class ValidationException extends RuntimeException {
    private final Set<String> errors;

    public ValidationException(Set<String> errors) {
        super("Errores de validación");
        this.errors = errors;
    }

    public Set<String> getErrors() {
        return errors;
    }
}
