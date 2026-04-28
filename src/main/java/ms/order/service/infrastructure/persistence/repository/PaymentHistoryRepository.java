package ms.order.service.infrastructure.persistence.repository;

import ms.order.service.infrastructure.persistence.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, UUID> {
}
