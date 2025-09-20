package custom_cooking_instructions_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaInstructionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;
    private final Logger logger = LoggerFactory.getLogger(KafkaInstructionProducer.class);

    public KafkaInstructionProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                    @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(Object payload, String key) {
        logger.info("Publishing instruction to topic {} key={}", topic, key);
        kafkaTemplate.send(topic, key, payload.toString());
    }
}