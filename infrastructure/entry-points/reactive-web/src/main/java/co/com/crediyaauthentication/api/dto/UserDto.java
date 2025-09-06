package co.com.crediyaauthentication.api.dto;


import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Usuario del sistema")
public record UserDto(

        @Schema(description = "Nombre del usuario", example = "Alejandra")
        String name,

        @Schema(description = "Apellido del usuario", example = "Gómez")
        String lastname,

        @Schema(description = "Correo electrónico válido", example = "aleja@example.com")
        String email,

        @Schema(description = "Documento de identidad obligatorio", example = "C123456789")
        String documentIdentification,

        @Schema(description = "Telefono de 10 dígitos", example = "3123757475")
        String phone,

        @Schema(description = "Salario base del usuario", example = "3500000")
        String baseSalary,

        @Schema(description = "Lista de roles obligatoria", example = "[1, 2]")
        List<Long> roles,

        @Schema(description = "Fecha de nacimiento", example = "01-07-1997")
        LocalDate birthday,

        @Schema(description = "Dirección válida", example = "cr 5 #8-9")
        String address
) {
}
