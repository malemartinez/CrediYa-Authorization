package co.com.crediyaauthentication.usecase.user;

import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.model.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.Exceptions.ValidationException;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.UserValidator;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private UserUseCase userUseCase;

    private User buildValidUser() {
        User user = new User();
        user.setName("Juan");
        user.setLastname("Pérez");
        user.setBaseSalary(5000000.0);
        user.setEmail("juan.perez@test.com");
        user.setIdRole(1L);
        return user;
    }
    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userValidator = mock(UserValidator.class);
        userUseCase = new UserUseCase(userRepository, roleRepository, userValidator);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        User user = buildValidUser();
        Role rol = Role.builder()
                .id(1L)
                .name("USER")
                .description("USUARIO GENÉRICO")
                .build();

        // validator no lanza excepción
        doNothing().when(userValidator).validate(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepository.findById(user.getIdRole())).thenReturn(Mono.just(rol));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userValidator).validate(user);
        verify(userRepository).findByEmail(user.getEmail());
        verify(roleRepository).findById(user.getIdRole());
        verify(userRepository).save(user);
    }

    @Test
    void shouldFailWhenValidationFails() {
        User user = buildValidUser();

        doThrow(new ValidationException(Set.of("error")))
                .when(userValidator).validate(user);

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(ValidationException.class)
                .verify();

        verify(userValidator).validate(user);
        verifyNoInteractions(roleRepository);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        User user = buildValidUser();

        doNothing().when(userValidator).validate(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("El email ya está registrado"))
                .verify();

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFailWhenRoleNotFound() {
        User user = buildValidUser();

        doNothing().when(userValidator).validate(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(roleRepository.findById(user.getIdRole())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.saveUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("El rol no existe"))
                .verify();

        verify(roleRepository).findById(user.getIdRole());
        verify(userRepository, never()).save(any());
    }
}