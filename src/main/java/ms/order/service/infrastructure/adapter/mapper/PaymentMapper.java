package ms.order.service.infrastructure.adapter.mapper;

import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.model.Payment;
import ms.order.service.infrastructure.dto.message.out.PaymentOrderMessage;
import ms.order.service.infrastructure.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PaymentMapper implements BaseMapper<Payment, PaymentEntity> {
/*
    @Mapping(target = "amount", source = "amount")
    public abstract PaymentEvent fromRequest(PaymentStatusMessage paymentEventRequest, BigDecimal amount);*/

     public abstract PaymentEntity  toEntity(Payment payment);

     public abstract Payment toDomain(PaymentEntity entity);

    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "orderId", source = "id")
    public abstract PaymentOrderMessage toPaymentOrderMessage(OrderOut order);
}
