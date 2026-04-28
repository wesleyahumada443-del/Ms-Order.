package ms.order.service.config;

import ms.order.service.utils.ObjetMapperUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "dummy" }, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
public class BaseKafkaIntegrationTest {

    protected static final String DEFAULT_GROUP_ID = "testGroup";
    private static final Logger log = LoggerFactory.getLogger(BaseKafkaIntegrationTest.class);
    @Autowired
    protected KafkaAdmin kafkaAdmin;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    protected void createTopic(String topicName, int partitions) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(topicName, partitions, (short) 1));
    }

    protected Consumer<String, String> createConsumer(String groupId, String topic) {
        Map<String, Object> props = KafkaTestUtils.consumerProps(groupId, "true", embeddedKafkaBroker);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, String> consumer = new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    // 🔄 Lee mensajes con timeout
    protected List<String> pollMessages(Consumer<String, String> consumer, Duration timeout) {
        var records = KafkaTestUtils.getRecords(consumer, timeout);
        var topic = consumer.subscription().iterator().next();
        var iterable = records.records(topic);

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(ConsumerRecord::value)
                .toList();
    }


    protected <T> void assertMessagePublished(String topic, T expectedObject) {
        try (var consumer = createConsumer(DEFAULT_GROUP_ID, topic)) {
            List<String> messages = pollMessages(consumer, Duration.ofSeconds(5));
            log.info("message received into test: {}", messages);
            assertFalse(messages.isEmpty(), "No se recibió ningún mensaje en topic: " + topic);

            String expectedJson = ObjetMapperUtils.toJson(expectedObject);
            assertEquals(expectedJson, messages.get(0), "El mensaje esperado no fue publicado en topic: " + topic);
        }
    }

    protected void assertNoMessageProduced(String topic, Duration timeout) {
        try (var consumer = createConsumer(DEFAULT_GROUP_ID, topic)) {
            List<String> messages = pollMessages(consumer, timeout);
            assertTrue(messages.isEmpty(), "Se recibieron mensajes inesperados en topic: " + topic);
        }
    }
}
