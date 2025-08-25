package co.com.crediyaauthentication.api.dto;


import java.util.Date;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UserDto(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        String lastname,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email no válido")
        String email,

        @NotBlank(message = "El documento es obligatorio")
        String documentIdentification,

        @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
        String phone,

        @Positive(message = "El salario debe ser mayor que cero")
        String baseSalary,

        @NotBlank(message = "El rol es obligatorio")
        Long idRole,
        Date birthday,
        String address
) {}
