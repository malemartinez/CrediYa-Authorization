package co.com.crediyaauthentication.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
