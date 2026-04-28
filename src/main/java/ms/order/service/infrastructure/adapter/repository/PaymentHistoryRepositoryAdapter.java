package ms.order.service.infrastructure.adapter.repository;

import ms.order.service.domain.exceptions.RecordNotFoundException;
import ms.order.service.domain.model.PaymentHistory;
import ms.order.service.domain.port.out.PaymentHistoryRepositoryOutPort;
import ms.order.service.infrastructure.adapter.mapper.PaymentHistoryMapper;
import ms.order.service.infrastructure.persistence.entity.PaymentHistoryEntity;
import ms.order.service.infrastructure.persistence.repository.PaymentHistoryRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Component
public class PaymentHistoryRepositoryAdapter extends BaseRepositoryAdapter<
        PaymentHistoryRepository,
        PaymentHistoryMapper,
        PaymentHistory,
        PaymentHistoryEntity,
        UUID> implements PaymentHistoryRepositoryOutPort {

    @Autowired
    public PaymentHistoryRepositoryAdapter(PaymentHistoryRepository repository, PaymentHistoryMapper mapper) {
        super(repository, mapper);
    }

}
