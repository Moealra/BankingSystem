package com.moayad.bank.domain;

public class SavingsAccount extends Account {

    private final double interestRate; // e.g., 0.02 = 2%

    public SavingsAccount(String accountNumber, double interestRate) {
        super(accountNumber);

        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
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
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds (no overdraft on savings)");
        }

        balance -= amount;
        addTransaction(new Transaction(TransactionType.WITHDRAW, amount, description));
    }

    public void applyMonthlyInterest() {
        if (getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        if (interestRate == 0) return;

        double interest = balance * interestRate;
        if (interest <= 0) return;

        balance += interest;
        addTransaction(new Transaction(TransactionType.DEPOSIT, interest, "Monthly interest"));
    }
}
