package ms.order.service.infrastructure.adapter.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ms.order.service.domain.exceptions.RecordNotFoundException;
import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.domain.port.out.BaseCRUDRepositoryOutPort;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Getter
@RequiredArgsConstructor
public class BaseRepositoryAdapter<
        REPOSITORY extends JpaRepository<ENTITY, ID>,
        MAPPER extends BaseMapper<MODEL, ENTITY>,
        MODEL,
        ENTITY,
        ID> implements BaseCRUDRepositoryOutPort<MODEL> {

    private final REPOSITORY repository;
    private final MAPPER mapper;

    @Override
    public List<MODEL> findByIds(List<UUID> ids) {
        var result = repository.findAllById((List<ID>) ids);
        if (result.isEmpty()) return null;
        return result.stream().map(mapper::toDomain).toList();
    }

    @Override
    public MODEL findById(UUID id) {
        return repository.findById((ID) id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<MODEL> findAll(Pageable pageable) {
        var page = repository.findAll(pageable);
        return Optional.of(page.getContent())
                .filter(ObjectUtils::isNotEmpty)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RecordNotFoundException("Don't have any records"));
    }

    @Override
    public MODEL create(MODEL model) {
        return mapper.toDomain(repository.save(mapper.toEntity(model)));
    }

    @Override
    public MODEL updateById(MODEL model) {
        return mapper.toDomain(repository.save(mapper.toEntity(model)));
    }

    @Override
    public void deleteById(UUID id) {
        if (repository.existsById((ID) id)) {
            repository.deleteById((ID) id);
        } else {
            throw new RecordNotFoundException("No record found with id " + id);
        }
    }
}
