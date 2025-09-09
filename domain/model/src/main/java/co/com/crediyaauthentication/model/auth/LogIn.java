package co.com.crediyaauthentication.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LogIn {

    private String email;
    private String password;
}
