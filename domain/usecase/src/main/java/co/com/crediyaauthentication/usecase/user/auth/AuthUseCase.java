package co.com.crediyaauthentication.usecase.user.auth;

import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.auth.LogIn;
import co.com.crediyaauthentication.model.auth.Token;
import co.com.crediyaauthentication.model.auth.gateways.AuthRepository;
import co.com.crediyaauthentication.model.auth.gateways.TokenProviderPort;
import co.com.crediyaauthentication.model.helpers.PasswordEncoderPort;
import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProviderPort;


    public Mono<Token> logIn(LogIn dto) {
        return userRepository.findByEmail(dto.getEmail())
                // validación de credenciales manual
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                // cargar roles desde el repositorio
                .flatMap(user -> authRepository.getRoleIdsByUserId(user)
                        .flatMap(roleRepository::findById)
                        .map(Role::getName)
                        .collectList()
                        .map(roles -> new Token(tokenProviderPort.generateToken(user.getEmail(),roles)))
                )
                .switchIfEmpty(Mono.error(new BusinessException("Credenciales incorrectas")));
    }
}
