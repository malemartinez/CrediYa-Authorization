package co.com.crediyaauthentication.r2dbc.config;

import co.com.crediyaauthentication.model.helpers.PasswordEncoderPort;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.model.user.UserValidator;
import co.com.crediyaauthentication.model.user.gateways.UserCasePort;
import co.com.crediyaauthentication.model.user.gateways.UserRepository;
import co.com.crediyaauthentication.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;


@Configuration
public class R2dbcReactiveConfig {
    @Bean
    public TransactionalOperator transactionalOperator(R2dbcTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    @Primary
    public UserCasePort userCasePort(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     UserValidator userValidator,
                                     TransactionalOperator transactionalOperator,
                                     PasswordEncoderPort passwordEncoder
    ) {

        UserUseCase pureUseCase = new UserUseCase(userRepository, roleRepository, userValidator , passwordEncoder );
        return new TransactionalUserUseCase(pureUseCase, transactionalOperator);
    }
}
