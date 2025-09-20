package custom_cooking_instructions_api.dto;

public class InstructionRequest {
    private Long orderId;
    private String text;
    private String customerName;

    // Getters & Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}