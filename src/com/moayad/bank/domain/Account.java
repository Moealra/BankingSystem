package com.moayad.bank.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private final String accountNumber;
    protected double balance;
    private AccountStatus status;
    private final List<Transaction> transactions;

    protected Account(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }

        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    protected void setStatus(AccountStatus status) {
        this.status = status;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    protected void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public abstract void deposit(double amount, String description);

    public abstract void withdraw(double amount, String description);
}
