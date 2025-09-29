package co.com.crediyaauthentication.model.user;
import co.com.crediyaauthentication.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String documentIdentification;
    private String phone;
    private Double baseSalary;
    private LocalDate birthday;
    private String address;
    private Set<Role> roles;
}

