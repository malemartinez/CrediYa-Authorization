package co.com.crediyaauthentication.r2dbc.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRoleEntity {
    private Long userId;
    private Long roleId;
}
