package ms.order.service.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

/**
 * This class is responsible for the configuration of Kafka.
 *
 * @author Williams Ahumada
 */
@Log4j2
@Configuration
public class KafkaConfig {//TODO

    @Bean
    public ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties, SslBundles sslBundles, AppProperties appProperties) {
        var configProps = kafkaProperties.buildConsumerProperties(sslBundles);
        setSecurityConfig(appProperties, configProps);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory,
                                                                                                 AppProperties appProperties,
                                                                                                 KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        var errorHandler = getDefaultErrorHandler(appProperties, kafkaTemplate);
        factory.setCommonErrorHandler(errorHandler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private DefaultErrorHandler getDefaultErrorHandler(AppProperties appProperties, KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate) {
            @Override
            public void accept(ConsumerRecord<?, ?> consumerRecord, Consumer<?, ?> consumer, Exception exception) {
                log.info("Message reached maximum retries. It will be sent to the dead letter queue (DLT)");
                super.accept(consumerRecord, consumer, exception);
            }
        };
        var errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer,
                new FixedBackOff(appProperties.getKafka().getConsumers().getRetries().getBackOffInterval(),
                        appProperties.getKafka().getConsumers().getRetries().getMaxAttempts())) {
            @Override
            public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> consumerRecord, Consumer<?, ?> consumer, MessageListenerContainer container) {
                var rootException = ExceptionUtils.getRootCause(thrownException);
                log.info("Error processing message. [{}] {}. Message will be sent to retry", rootException.getClass().getSimpleName(), rootException.getMessage());
                return super.handleOne(thrownException, consumerRecord, consumer, container);
            }
        };
        errorHandler.setSeekAfterError(false);
        return errorHandler;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, SslBundles sslBundles, AppProperties appProperties) {
        var configProps = kafkaProperties.buildProducerProperties(sslBundles);
        setSecurityConfig(appProperties, configProps);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    private void setSecurityConfig(AppProperties appProperties, Map<String, Object> configProps) {
        if (!"AWS_MSK_IAM".equals(appProperties.getKafka().getSecurity().getSaslMechanism())) {
            String jaasTemplate = appProperties.getKafka().getSecurity().getJaasTemplate();
            String jaasCfg = String.format(jaasTemplate, appProperties.getKafka().getSecurity().getUser(), appProperties.getKafka().getSecurity().getPassword());
            configProps.put("sasl.jaas.config", jaasCfg);
            configProps.put("sasl.mechanism", appProperties.getKafka().getSecurity().getSaslMechanism());
        }
    }
}
