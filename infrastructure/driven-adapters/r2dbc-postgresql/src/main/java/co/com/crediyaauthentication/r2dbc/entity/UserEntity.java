package co.com.crediyaauthentication.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @Column("id_usuario")
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private LocalDate birthday;
    private String address;
    private String phone;

    @Column("document_identification")
    private String documentIdentification;

    @Column("base_salary")
    private Double baseSalary;

}
