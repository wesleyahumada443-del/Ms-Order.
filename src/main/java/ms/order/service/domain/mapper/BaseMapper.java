package ms.order.service.domain.mapper;

import java.util.List;

public interface BaseMapper<D,E> {
    D toDomain(E entity);
    E toEntity(D domain);
    List<D> toDomain(List<E> entities);
    List<E> toEntity(List<D> domains);
}
