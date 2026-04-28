package ms.order.service.infrastructure.adapter.message.kafka.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import ms.order.service.infrastructure.dto.PayloadMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for producer kafka event @ExampleTemplateRequest.
 *
 * @author Williams Ahumada
 */
@Log4j2
@Component
public class KafkaMessageProducer {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageProducer(ObjectMapper mapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.mapper = mapper;
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    public <T extends PayloadMessage> void sendMessage(String topicName, T  message) {
        log.info("Sending message to Kafka topic: {}", topicName);
        String stringMessage = mapper.writeValueAsString(message);
        log.info("Sending message to Kafka topic: {}, message: {}", topicName, stringMessage);
        kafkaTemplate.send(topicName, stringMessage);
        log.info("Sent message to Kafka topic: {} successfully", topicName);
    }
}
