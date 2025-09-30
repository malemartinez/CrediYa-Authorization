package co.com.crediyaauthentication.api.auth;

import co.com.crediyaauthentication.api.error.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "logInUser",
                    operation = @Operation(
                            operationId = "logIn",
                            summary = "Iniciar Sesión",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Autentica al usuario y devuelve el token de acceso",
                                    content = @Content(
                                            schema = @Schema(implementation = LogInDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login correcto",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = TokenResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error al iniciar sesión",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiErrorResponse.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> AuhtRouterFunction(AuthHandler handler) {
        return route(POST("/api/v1/auth/login"), handler::logInUser);

    }
}
