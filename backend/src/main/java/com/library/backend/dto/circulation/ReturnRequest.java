package com.library.backend.dto.circulation;

public class ReturnRequest {
    private Long transactionId;
    private String barcode;
    private String copyCondition;

    public ReturnRequest() {}

    public ReturnRequest(Long transactionId, String barcode, String copyCondition) {
        this.transactionId = transactionId;
        this.barcode = barcode;
        this.copyCondition = copyCondition;
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getCopyCondition() { return copyCondition; }
    public void setCopyCondition(String copyCondition) { this.copyCondition = copyCondition; }
}
