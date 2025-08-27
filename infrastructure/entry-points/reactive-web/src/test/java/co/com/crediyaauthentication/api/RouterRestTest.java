package co.com.crediyaauthentication.api;

import co.com.crediyaauthentication.api.dto.UserDto;
import co.com.crediyaauthentication.api.mapper.UserMapper;
import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.model.user.gateways.UserCasePort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({RouterRest.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserCasePort userCasePort;

    @Autowired
    private UserMapper userMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserCasePort userCasePort() {
            return Mockito.mock(UserCasePort.class);
        }

        @Bean
        UserMapper userMapper() {
            return Mockito.mock(UserMapper.class);
        }
    }

    @Test
    void testListenSaveUser() {
        // Arrange: mockear handler
        User user = new User();
        user.setName("Juan");
        user.setLastname("Pérez");
        user.setEmail("juan@test.com");
        user.setBaseSalary(5000000.0);
        user.setIdRole(1L);

        UserDto dto = new UserDto(
                "Alejandra",
                "Gómez",
                "aleja@example.com",
                "C123456789",
                "3123757475",
                "3500000",
                1L,
                LocalDate.of(1997, 7, 1),
                "cr 5 #8-9"
        );

        // mocks mapper
        Mockito.when(userMapper.toDomain(Mockito.any())).thenReturn(user);
        Mockito.when(userMapper.toResponse(Mockito.any())).thenReturn(dto);

        // mocks useCase
        Mockito.when(userCasePort.saveUser(Mockito.any())).thenReturn(Mono.just(user));

        // Act & Assert: llamar la ruta con POST
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(response -> {
                    org.assertj.core.api.Assertions.assertThat(response.getName()).isEqualTo("Juan");
                    org.assertj.core.api.Assertions.assertThat(response.getEmail()).isEqualTo("juan@test.com");
                });
    }

}
