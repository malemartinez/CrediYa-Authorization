package co.com.crediyaauthentication.api.security.config;

import co.com.crediyaauthentication.api.security.GlobalSecurityExceptionHandler;
import co.com.crediyaauthentication.api.security.jwt.JwtAuthenticationManager;
import co.com.crediyaauthentication.api.security.jwt.JwtServerAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtServerAuthenticationConverter jwtServerAuthenticationConverter;
    private final GlobalSecurityExceptionHandler globalSecurityExceptionHandler;

    public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager, JwtServerAuthenticationConverter jwtServerAuthenticationConverter, GlobalSecurityExceptionHandler globalSecurityExceptionHandler) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtServerAuthenticationConverter = jwtServerAuthenticationConverter;
        this.globalSecurityExceptionHandler = globalSecurityExceptionHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        AuthenticationWebFilter jwtAuthFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        jwtAuthFilter.setServerAuthenticationConverter(jwtServerAuthenticationConverter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/api/v1/usuarios").hasAnyRole("ADMIN", "ASSESSOR")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(globalSecurityExceptionHandler)
                        .accessDeniedHandler(globalSecurityExceptionHandler)
                )
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

