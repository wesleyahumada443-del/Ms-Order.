package ms.order.service.infrastructure.adapter.message.kafka.in;

import lombok.extern.log4j.Log4j2;
import ms.order.service.application.usecase.ChangeOrderUseCase;
import ms.order.service.infrastructure.adapter.mapper.PaymentHistoryMapper;
import ms.order.service.infrastructure.dto.message.in.PaymentStatusMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Objects.isNull;
import static ms.order.service.infrastructure.utils.Constants.Info.Traces.END;
import static ms.order.service.infrastructure.utils.Constants.Info.Traces.INIT;

@Log4j2
@Component
public class PaymentOrderStatusKafkaConsumerAdapter extends AbstractKafkaProcessThreadsConsumeAdapter<PaymentStatusMessage> {

    private final ChangeOrderUseCase changeOrderUseCase;
    private final PaymentHistoryMapper paymentHistoryMapper;

    public PaymentOrderStatusKafkaConsumerAdapter(RetryTemplate retryTemplate,
                                                  ThreadPoolTaskExecutor taskExecutor,
                                                  ChangeOrderUseCase changeOrderUseCase,
                                                  PaymentHistoryMapper paymentHistoryMapper) {

        super(PaymentStatusMessage.class, taskExecutor, retryTemplate);
        this.changeOrderUseCase = changeOrderUseCase;
        this.paymentHistoryMapper = paymentHistoryMapper;
    }

    @KafkaListener(topics = "${app.config.kafka.consumers.topic-payment-notification}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${app.config.kafka.consumers.group-id}")
    public void consumeMessage(ConsumerRecord<String, String> message, Acknowledgment acknowledgment) {
        log.info("Received Kafka message OrderKafkaConsumer");
        processConsume(message, acknowledgment);
    }


    @Override
    public void execute(PaymentStatusMessage event, UUID traceId) {
        if (!isNull(event)) {
            log.info(INIT.formatted("payment order"));
            changeOrderUseCase.changePaymentOrder(paymentHistoryMapper.fromRequest(event));
        }
            log.info(END.formatted("payment order"));
    }
}
