package com.moayad.bank.domain;

public class ChequingAccount extends Account {

    private final double overdraftLimit; // e.g. 500 means balance can go to -500

    public ChequingAccount(String accountNumber, double overdraftLimit) {
        super(accountNumber);

        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void deposit(double amount, String description) {
        if (getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance += amount;
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount, description));
    }

    @Override
    public void withdraw(double amount, String description) {
        if (getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (balance - amount < -overdraftLimit) {
            throw new IllegalStateException("Overdraft limit exceeded");
        }

        balance -= amount;
        addTransaction(new Transaction(TransactionType.WITHDRAW, amount, description));
    }
}
