package co.com.crediyaauthentication.model.user;

import co.com.crediyaauthentication.model.Exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    private User buildValidUser() {
        User user = new User();
        user.setName("Juan");
        user.setLastname("Pérez");
        user.setBaseSalary(5000000.0);
        user.setEmail("juan.perez@test.com");
        return user;
    }

    @Test
    void shouldValidateCorrectUser() {
        User user = buildValidUser();

        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        User user = buildValidUser();
        user.setName(" ");

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El nombre no puede estar vacío"));
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        User user = buildValidUser();
        user.setLastname("");

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El apellido no puede estar vacío"));
    }

    @Test
    void shouldFailWhenSalaryIsNull() {
        User user = buildValidUser();
        user.setBaseSalary(null);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El salario no puede estar vacío"));
    }

    @Test
    void shouldFailWhenSalaryIsNegative() {
        User user = buildValidUser();
        user.setBaseSalary(-1000.0);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El salario debe estar entre 0 y 15.000.000"));
    }

    @Test
    void shouldFailWhenSalaryIsTooHigh() {
        User user = buildValidUser();
        user.setBaseSalary(20000000.0);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El salario debe estar entre 0 y 15.000.000"));
    }

    @Test
    void shouldFailWhenEmailIsEmpty() {
        User user = buildValidUser();
        user.setEmail("");

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("El correo no puede estar vacío"));
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        User user = buildValidUser();
        user.setEmail("invalid-email");

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(user));

        assertTrue(ex.getErrors().contains("Formato de correo inválido"));
    }
}
