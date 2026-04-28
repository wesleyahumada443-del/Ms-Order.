package ms.order.service.infrastructure.adapter.message.kafka.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.infrastructure.dto.PayloadMessage;
import ms.order.service.domain.exceptions.OrderException;
import ms.order.service.infrastructure.utils.Constants;
import ms.order.service.infrastructure.utils.TraceIdUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;

import static java.lang.String.format;
import static ms.order.service.infrastructure.utils.ObjectMapperUtils.getMapper;

@Log4j2
@AllArgsConstructor
public abstract class AbstractKafkaProcessThreadsConsumeAdapter<T extends PayloadMessage> extends AbstractProcessHeaderAdapter {
    private Class<T> clazz;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    private final RetryTemplate retryTemplate;

    public static final String CONSUME_MESSAGE_METHOD = "consume%s";

    public void processConsume(ConsumerRecord<String, String> message, Acknowledgment acknowledgment) {
        var consumer = format(CONSUME_MESSAGE_METHOD, clazz.getSimpleName());

        log.info("[{}] - Message received: {}", getMessageIdFromHeader(message), message.value());
        try {
            var traceId = getTraceId(message);
            TraceIdUtils.setTraceId(traceId);

            var objectMessage = getMapper().readValue(message.value(), clazz);
            taskExecutor.execute(() -> processWithRetry(traceId, objectMessage, acknowledgment));

            log.info(format(Constants.Info.Traces.PROCESSED_SUCCESSFULLY, consumer, message.value()));
            log.info("Message consumed: {}", traceId);
        } catch (JsonProcessingException ex) {
            log.error(format(Constants.Error.Exceptions.PROCESSING_FORMAT_ERROR, consumer, message.value(), ex.getMessage()));
            log.error("Error processing message: {}.\n Error: {}, Exception: {}", message.value(), "Message sent has incorrect format!", ex.getMessage());
            acknowledgment.acknowledge();//TODO
        } catch (RejectedExecutionException ex) {
            //  Case when there are not enough threads available TODO
            log.error(format(Constants.Error.Exceptions.TASK_REJECTED_ERROR, consumer, message.value()), ex.getMessage());
            acknowledgment.nack(Duration.ofSeconds(5)); // Rejects the message so that Kafka retries after 5 seconds TODO
        } catch (Exception ex) {
            log.error("Error processing message: {}.\n Error: {}", message.value(), ex.getMessage());
            acknowledgment.acknowledge();
        }
    }

    private void processWithRetry(UUID traceId, T payload, Acknowledgment acknowledgment) {

        var consumer = format(CONSUME_MESSAGE_METHOD, clazz.getSimpleName());
        TraceIdUtils.setTraceId(traceId);
        retryTemplate.execute(context -> {
            try {
                execute(payload, traceId);
            } catch (OrderException ex) {
                log.error("OrderException skipping retry. error: {}", ex.getMessage(), ex);
                acknowledgment.acknowledge();
            }
            return null;
        });

        acknowledgment.acknowledge();
        log.info(format(Constants.Info.Traces.PROCESSED_SUCCESSFULLY, consumer, traceId));
    }

    public abstract void execute(T payload, UUID traceId);
}
