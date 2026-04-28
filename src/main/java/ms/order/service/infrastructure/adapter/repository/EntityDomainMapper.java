package ms.order.service.infrastructure.adapter.repository;

public interface EntityDomainMapper<DOMAIN, ENTITY> {
    DOMAIN toDomain(ENTITY entity);
    ENTITY toEntity(DOMAIN domain);
}
