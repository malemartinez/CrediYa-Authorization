package co.com.crediyaauthentication.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate birthday;
    private String address;
    private String phone;

    @Column("document_identification")
    private String documentIdentification;

    @Column("base_salary")
    private Double baseSalary;

    @Column("id_role")
    private Long idRole;

}
