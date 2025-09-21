package custom_cooking_instructions_api.service;

import custom_cooking_instructions_api.Entity.Instruction;
import custom_cooking_instructions_api.repository.InstructionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaInstructionConsumer {

    private final InstructionRepository repository;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(KafkaInstructionConsumer.class);

    public KafkaInstructionConsumer(InstructionRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "kitchen-group")
    public void consume(String message) {
        try {
            // Deserialize JSON message to Instruction
            Instruction payload = objectMapper.readValue(message, Instruction.class);

            logger.info("Received instruction for order {}: {}", payload.getOrderId(), payload.getText());

            // Update DB status
            repository.findById(payload.getId()).ifPresentOrElse(ins -> {
                ins.setStatus("PROCESSED");
                repository.save(ins);

                // Simulate forwarding to kitchen
                logger.info("Instruction {} marked as PROCESSED and delivered to kitchen", ins.getId());
            }, () -> {
                logger.warn("Instruction with id {} not found in DB; ignoring", payload.getId());
            });

        } catch (Exception e) {
            logger.error("Failed to process Kafka message: {}", message, e);
        }
    }
}