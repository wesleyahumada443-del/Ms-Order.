package ms.order.service.infrastructure.adapter.repository;

import ms.order.service.domain.model.Payment;
import ms.order.service.domain.model.PaymentHistory;
import ms.order.service.domain.port.out.PaymentHistoryRepositoryOutPort;
import ms.order.service.domain.port.out.PaymentOutPort;
import ms.order.service.infrastructure.adapter.mapper.PaymentHistoryMapper;
import ms.order.service.infrastructure.adapter.mapper.PaymentMapper;
import ms.order.service.infrastructure.persistence.entity.PaymentEntity;
import ms.order.service.infrastructure.persistence.entity.PaymentHistoryEntity;
import ms.order.service.infrastructure.persistence.repository.PaymentHistoryRepository;
import ms.order.service.infrastructure.persistence.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Component
public class PaymentRepositoryAdapter extends BaseRepositoryAdapter<
        PaymentRepository,
        PaymentMapper,
        Payment,
        PaymentEntity,
        UUID> implements PaymentOutPort {

    @Autowired
    public PaymentRepositoryAdapter(PaymentRepository repository, PaymentMapper mapper) {
        super(repository, mapper);
    }

}
