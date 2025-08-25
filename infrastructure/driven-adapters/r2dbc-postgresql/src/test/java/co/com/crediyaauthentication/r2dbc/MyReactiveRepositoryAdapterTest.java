package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.user.User;
import co.com.crediyaauthentication.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

//    @Test
//    void mustFindValueById() {
//
//        when(repository.findById("1")).thenReturn(Mono.just("test"));
//        when(mapper.map("test", Object.class)).thenReturn("test");
//
//        Mono<User> result = repositoryAdapter.findById("1");
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals("test"))
//                .verifyComplete();
//    }

//    @Test
//    void mustFindAllValues() {
//        when(repository.findAll()).thenReturn(Flux.just("test"));
//        when(mapper.map("test", Object.class)).thenReturn("test");
//
//        Flux<User> result = repositoryAdapter.findAll();
//
//        StepVerifier.create(result)
//                .expectNextMatches(value -> value.equals("test"))
//                .verifyComplete();
//    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<User> result = repositoryAdapter.findByExample(new User());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save(new UserEntity())).thenReturn(Mono.just(new UserEntity()));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<User> result = repositoryAdapter.save(new User());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(new UserEntity()))
                .verifyComplete();
    }
}
