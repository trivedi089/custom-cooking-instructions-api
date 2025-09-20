package custom_cooking_instructions_api.repository;

import custom_cooking_instructions_api.Entity.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    // Optional: find instructions by orderId
    List<Instruction> findByOrderId(Long orderId);
}