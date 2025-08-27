package co.com.crediyaauthentication.api.dto;


import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Usuario del sistema")
public record UserDto(

        @Schema(description = "Nombre del usuario", example = "Alejandra")
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Schema(description = "Apellido del usuario", example = "Gómez")
        @NotBlank(message = "El apellido es obligatorio")
        String lastname,

        @Schema(description = "Correo electrónico válido", example = "aleja@example.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email no válido")
        String email,

        @Schema(description = "Documento de identidad obligatorio", example = "C123456789")
        @NotBlank(message = "El documento es obligatorio")
        String documentIdentification,

        @Schema(description = "Telefono de 10 dígitos", example = "3123757475")
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
        String phone,

        @Schema(description = "Salario base del usuario", example = "3500000")
        @Positive(message = "El salario debe ser mayor que cero")
        String baseSalary,

        @Schema(description = "Rol obligatorio", example = "1")
        @NotBlank(message = "El rol es obligatorio")
        Long idRole,

        @Schema(description = "Fecha de nacimiento", example = "01-07-1997")
        LocalDate birthday,

        @Schema(description = "Dirección válida", example = "cr 5 #8-9")
        String address
) {}
