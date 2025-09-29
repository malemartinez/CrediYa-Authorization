package co.com.crediyaauthentication.api.security.jwt;

import co.com.crediyaauthentication.model.auth.gateways.TokenProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenProviderPort jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username = jwtService.extractSubject(authToken);
        if (username == null || !jwtService.validateToken(authToken)) {
            return Mono.empty();
        }

        List<String> roles = jwtService.extractRoles(authToken);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }
}
