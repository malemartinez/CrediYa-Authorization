package co.com.crediyaauthentication.r2dbc;

import co.com.crediyaauthentication.model.role.Role;
import co.com.crediyaauthentication.model.role.gateways.RoleRepository;
import co.com.crediyaauthentication.r2dbc.entity.RoleEntity;
import co.com.crediyaauthentication.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleEntity/* change for adapter model */,
        Long,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Flux<Role> findAllById(List<Long> ids) {
        return repository.findAllByIdIn(ids)
                .map(u -> mapper.map(u,Role.class));
    }
}
