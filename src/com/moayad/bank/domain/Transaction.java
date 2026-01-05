package com.moayad.bank.domain;

import java.time.LocalDateTime;

public class Transaction {

    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(TransactionType type, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

@Override
public String toString() {
    return timestamp + " | " + type + " | $" + amount + " | " + description;
}
}
