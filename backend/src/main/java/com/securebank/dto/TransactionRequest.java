package com.securebank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for adding a flagged transaction (insert).
 */
public class TransactionRequest {

    @NotBlank(message = "transactionId is required")
    private String transactionId;

    @NotNull(message = "amount is required")
    private Double amount;

    private String reason;

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
