package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.r2dbc.entity.RoleEntity;
import co.com.crediyaauthentication.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleEntity/* change for adapter model */,
        Long,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

}
