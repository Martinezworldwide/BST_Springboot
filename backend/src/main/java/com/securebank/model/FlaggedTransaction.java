package com.securebank.model;

import java.time.Instant;

/**
 * Represents a single flagged (fraudulent or suspicious) transaction stored in the BST.
 * Ordered by transactionId for BST hierarchy.
 */
public class FlaggedTransaction {

    private String transactionId;
    private double amount;
    private Instant flaggedAt;
    private String reason;

    public FlaggedTransaction() {}

    public FlaggedTransaction(String transactionId, double amount, Instant flaggedAt, String reason) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.flaggedAt = flaggedAt != null ? flaggedAt : Instant.now();
        this.reason = reason;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Instant getFlaggedAt() { return flaggedAt; }
    public void setFlaggedAt(Instant flaggedAt) { this.flaggedAt = flaggedAt; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
