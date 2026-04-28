package ms.order.service.infrastructure.adapter.message.kafka.out;

import lombok.RequiredArgsConstructor;
import ms.order.service.config.AppProperties;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.port.out.PaymentOrderOutPort;
import ms.order.service.infrastructure.adapter.mapper.PaymentMapper;
import org.springframework.stereotype.Component;


/**
 * This class adapter OUT is responsible for pre-producer kafka event @PaymentOrderMessage.
 *
 * @author Williams Ahumada
 */
@Component
@RequiredArgsConstructor
public class PaymentOrderKafkaProducerAdapter implements PaymentOrderOutPort {

    private final AppProperties appProperties;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final PaymentMapper paymentMapper;

    public void sendMessage(OrderOut orderOut) {
        kafkaMessageProducer.sendMessage(appProperties.getKafka().getProducers().getTopicPaymentOrder(),
                paymentMapper.toPaymentOrderMessage(orderOut));
    }
}
