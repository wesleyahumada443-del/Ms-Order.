package ms.order.service.domain.port.out;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BaseCRUDRepositoryOutPort<M> {
    List<M> findByIds(List<UUID> ids);
    M findById(UUID id);
    List<M> findAll(Pageable pageable);
    M create(M model);
    M updateById(M model);
    void deleteById(UUID id);
}
