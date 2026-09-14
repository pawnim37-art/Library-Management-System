package com.library.backend.dto.fine;

public class FinePayRequest {
    private String paymentMethod;
    private String reason;

    public FinePayRequest() {}

    public FinePayRequest(String paymentMethod, String reason) {
        this.paymentMethod = paymentMethod;
        this.reason = reason;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
