package ms.order.service.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class holds the application properties.
 * It uses the ConfigurationProperties annotation to bind the properties from the application configuration.
 *
 * @author Williams Ahumada
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "app.config")
public class AppProperties {
    private KafkaConfig kafka = new KafkaConfig();

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class KafkaConfig {
        private SecurityConfig security = new SecurityConfig();
        private ConsumerConfig consumers = new ConsumerConfig();
        private ProducerConfig producers = new ProducerConfig();

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        public static class SecurityConfig {
            private String saslMechanism;
            private String user;
            private String password;
            private String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        }

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        public static class ConsumerConfig {
            private String groupId;
            private RetriesConfig retries;

            @Getter
            @Setter
            @ToString
            @NoArgsConstructor
            public static class RetriesConfig {
                private Long backOffInterval = 100000L;
                private Long maxAttempts = 3L;
            }
        }

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        public static class ProducerConfig {
            private String topicPaymentNotification;
            private String topicPaymentOrder;
        }
    }
}
