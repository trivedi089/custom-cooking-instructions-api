package custom_cooking_instructions_api.service;

import custom_cooking_instructions_api.Entity.Instruction;
import custom_cooking_instructions_api.repository.InstructionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaInstructionConsumer {

    private final InstructionRepository repository;
    private final Logger logger = LoggerFactory.getLogger(KafkaInstructionConsumer.class);

    public KafkaInstructionConsumer(InstructionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "kitchen-group")
    public void consume(String message) {
        logger.info("Received instruction message: {}", message);
        // For now just log; in real app parse JSON and update DB if needed
    }
}