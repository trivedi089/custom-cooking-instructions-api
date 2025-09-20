package custom_cooking_instructions_api.controller;

import custom_cooking_instructions_api.Entity.Instruction;
import custom_cooking_instructions_api.service.InstructionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instructions")
public class InstructionController {

    private final InstructionService service;

    public InstructionController(InstructionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Instruction> createInstruction(@RequestBody Instruction instruction) {
        Instruction saved = service.createInstruction(instruction);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instruction> getInstruction(@PathVariable Long id) {
        return service.getInstruction(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}