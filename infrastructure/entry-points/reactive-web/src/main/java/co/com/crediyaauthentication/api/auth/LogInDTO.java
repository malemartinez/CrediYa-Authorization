package co.com.crediyaauthentication.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para iniciar sesión")
public record LogInDTO(

        @Schema(description = "Email del usuario", example = "Alejandra@mail.com")
        String email,

        @Schema(description = "Contraseña del usuario", example = "unaContraseñaSegura")
        String password) {}
