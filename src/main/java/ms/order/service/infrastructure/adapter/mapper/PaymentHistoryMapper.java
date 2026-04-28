package ms.order.service.infrastructure.adapter.mapper;

import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.domain.model.PaymentEvent;
import ms.order.service.domain.model.PaymentHistory;
import ms.order.service.infrastructure.dto.message.in.PaymentStatusMessage;
import ms.order.service.infrastructure.persistence.entity.PaymentHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PaymentHistoryMapper implements BaseMapper<PaymentHistory, PaymentHistoryEntity> {

    @Mapping(target = "orderId", source = "sourceId")
    @Mapping(target = "id", source = "paymentId")
    @Mapping(target = "status", source = "status")
    public abstract PaymentEvent fromRequest(PaymentStatusMessage paymentEventRequest);

}
