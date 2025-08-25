package co.com.crediyaauthentication.config;

import co.com.crediyaauthentication.model.user.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserValidatorConfig {

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }
}
