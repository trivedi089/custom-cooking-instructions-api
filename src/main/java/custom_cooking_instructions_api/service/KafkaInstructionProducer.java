package custom_cooking_instructions_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaInstructionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RetryTemplate retryTemplate;
    private final String topic;
    private final Logger logger = LoggerFactory.getLogger(KafkaInstructionProducer.class);
    private final ObjectMapper objectMapper;

    public KafkaInstructionProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                    @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.objectMapper = new ObjectMapper();

        // Configure retry with exponential backoff
        this.retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // retry 3 times
        retryTemplate.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 1 second
        backOffPolicy.setMultiplier(2.0);       // double interval each retry
        backOffPolicy.setMaxInterval(10000);    // max 10 seconds
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    public void send(Object payload, String key) {
        try {
            // Serialize payload to JSON
            String message = objectMapper.writeValueAsString(payload);

            // Retry sending
            retryTemplate.execute(context -> {
                logger.info("Publishing instruction to topic {} key={}", topic, key);
                kafkaTemplate.send(topic, key, message).get(); // block until ack
                return null;
            });

        } catch (Exception e) {
            logger.error("Failed to send instruction to Kafka. Key={}, Payload={}", key, payload, e);
            throw new RuntimeException("Kafka send failed", e);
        }
    }
}