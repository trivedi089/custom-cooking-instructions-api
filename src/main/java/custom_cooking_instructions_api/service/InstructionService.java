package custom_cooking_instructions_api.service;

import custom_cooking_instructions_api.Entity.Instruction;
import custom_cooking_instructions_api.repository.InstructionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InstructionService {

    private final InstructionRepository repository;
    private final KafkaInstructionProducer producer; // assume this exists

    public InstructionService(InstructionRepository repository, KafkaInstructionProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @Transactional
    public Instruction createInstruction(Instruction instruction) {
        // Step 1: Save in DB with RECEIVED status
        instruction.setStatus("RECEIVED");
        Instruction saved = repository.save(instruction);

        // Step 2: Publish to Kafka
        try {
            producer.send(saved, String.valueOf(saved.getId()));
            saved.setStatus("PUBLISHED");
        } catch (Exception e) {
            saved.setStatus("FAILED");
        }

        // Step 3: Update DB with new status
        return repository.save(saved);
    }

    public Optional<Instruction> getInstruction(Long id) {
        return repository.findById(id);
    }
}