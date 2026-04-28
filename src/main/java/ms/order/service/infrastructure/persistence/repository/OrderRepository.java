package ms.order.service.infrastructure.persistence.repository;

import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
