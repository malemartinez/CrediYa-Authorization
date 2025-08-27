package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.user.Exceptions.BusinessException;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    // Define un usuario y su entidad para los mocks
    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Inicializa los objetos antes de cada prueba
        user = User.builder().email("test@mail.com").build();
        userEntity = UserEntity.builder().email("test@mail.com").build();

    }

    @Test
    void shouldSaveUserSuccessfully() {

        // Mapeo genérico del mapper que se usa en el constructor y el método save
        when(mapper.map(any(), any())).thenReturn(userEntity).thenReturn(user);
        // Escenario: El guardado es exitoso
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(savedUser -> savedUser.getEmail().equals(user.getEmail()))
                .verifyComplete();
        verify(repository, times(1)).save(any(UserEntity.class));
    }
    @Test
    void shouldHandleDataIntegrityViolationExceptionOnSave() {

        // Mapeo genérico del mapper que se usa en el constructor y el método save
        when(mapper.map(any(), any())).thenReturn(userEntity).thenReturn(user);
        // Escenario: El guardado lanza una excepción de integridad de datos
        when(repository.save(any(UserEntity.class)))
                .thenReturn(Mono.error(new DataIntegrityViolationException("Error de datos")));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException)
                .verify();
        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void shouldHandleGenericExceptionOnSave() {

        // Mapeo genérico del mapper que se usa en el constructor y el método save
        when(mapper.map(any(), any())).thenReturn(userEntity).thenReturn(user);
        // Escenario: El guardado lanza una excepción genérica
        when(repository.save(any(UserEntity.class)))
                .thenReturn(Mono.error(new RuntimeException("Error inesperado")));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
        verify(repository, times(1)).save(any(UserEntity.class));
    }
    @Test
    void shouldFindUserByEmailSuccessfully() {
        // Escenario: Se encuentra un usuario por email
        when(repository.findByEmail("test@mail.com")).thenReturn(Mono.just(user));

        Mono<User> result = repositoryAdapter.findByEmail("test@mail.com");

        StepVerifier.create(result)
                .expectNextMatches(foundUser -> foundUser.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }
    @Test
    void shouldReturnEmptyMonoWhenUserNotFound() {
        // Escenario: No se encuentra ningún usuario por email
        when(repository.findByEmail("non-existent@mail.com")).thenReturn(Mono.empty());

        Mono<User> result = repositoryAdapter.findByEmail("non-existent@mail.com");

        StepVerifier.create(result)
                .expectNextCount(0) // Espera que no se emita ningún elemento
                .verifyComplete();
    }

    @Test
    void shouldHandleExceptionWhenFindingByEmail() {
        // Escenario: Ocurre un error al buscar por email
        when(repository.findByEmail("test@mail.com"))
                .thenReturn(Mono.error(new RuntimeException("Error de conexión")));

        Mono<User> result = repositoryAdapter.findByEmail("test@mail.com");

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

}
