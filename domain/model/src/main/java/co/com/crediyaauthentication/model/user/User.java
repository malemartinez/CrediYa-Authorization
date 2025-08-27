package co.com.crediyaauthentication.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String name;
    private String lastname;
    private String email;
    private String documentIdentification;
    private String phone;
    private Double baseSalary;
    private Long idRole;
    private LocalDate birthday;
    private String address;
}

