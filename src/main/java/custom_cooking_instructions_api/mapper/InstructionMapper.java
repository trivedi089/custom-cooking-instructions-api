package custom_cooking_instructions_api.mapper;

import custom_cooking_instructions_api.dto.InstructionRequest;
import custom_cooking_instructions_api.dto.InstructionResponse;
import custom_cooking_instructions_api.Entity.Instruction;

public class InstructionMapper {

    // Convert Request DTO → Entity
    public static Instruction toEntity(InstructionRequest request) {
        Instruction instruction = new Instruction();
        instruction.setOrderId(request.getOrderId());
        instruction.setText(request.getText());
        instruction.setCustomerName(request.getCustomerName());
        return instruction;
    }

    // Convert Entity → Response DTO
    public static InstructionResponse toResponse(Instruction instruction) {
        InstructionResponse response = new InstructionResponse();
        response.setId(instruction.getId());
        response.setOrderId(instruction.getOrderId());
        response.setText(instruction.getText());
        response.setCustomerName(instruction.getCustomerName());
        response.setStatus(instruction.getStatus());
        response.setCreatedAt(instruction.getCreatedAt());
        return response;
    }
}