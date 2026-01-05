package com.moayad.bank.service;

import com.moayad.bank.domain.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankService {

    private final Map<String, Customer> customersById = new LinkedHashMap<>();
    private final Map<String, Account> accountsByNumber = new LinkedHashMap<>();

    // ---------- Customers ----------

    public Customer createCustomer(String customerId, String fullName) {
        if (customersById.containsKey(customerId)) {
            throw new IllegalArgumentException("Customer already exists: " + customerId);
        }
        Customer customer = new Customer(customerId, fullName);
        customersById.put(customerId, customer);
        return customer;
    }

    public Customer getCustomer(String customerId) {
        Customer c = customersById.get(customerId);
        if (c == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        return c;
    }

    public Collection<Customer> getAllCustomers() {
        return customersById.values();
    }

    // ---------- Accounts ----------

    public SavingsAccount openSavingsAccount(String customerId, String accountNumber, double interestRate) {
        ensureAccountDoesNotExist(accountNumber);

        Customer customer = getCustomer(customerId);
        SavingsAccount acc = new SavingsAccount(accountNumber, interestRate);

        accountsByNumber.put(accountNumber, acc);
        customer.addAccount(acc);

        return acc;
    }

    public ChequingAccount openChequingAccount(String customerId, String accountNumber, double overdraftLimit) {
        ensureAccountDoesNotExist(accountNumber);

        Customer customer = getCustomer(customerId);
        ChequingAccount acc = new ChequingAccount(accountNumber, overdraftLimit);

        accountsByNumber.put(accountNumber, acc);
        customer.addAccount(acc);

        return acc;
    }

    public Account getAccount(String accountNumber) {
        Account acc = accountsByNumber.get(accountNumber);
        if (acc == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return acc;
    }

    public Collection<Account> getAllAccounts() {
        return accountsByNumber.values();
    }

    // ---------- Balance helpers for UI ----------

    public double getBalance(String accountNumber) {
        return getAccount(accountNumber).getBalance();
    }

    public double getAvailableFunds(String accountNumber) {
        Account acc = getAccount(accountNumber);

        // Savings: available = balance (no overdraft)
        if (acc instanceof SavingsAccount) {
            return acc.getBalance();
        }

        // Chequing: available = balance + overdraft limit
        if (acc instanceof ChequingAccount) {
            ChequingAccount chq = (ChequingAccount) acc;
            return chq.getBalance() + chq.getOverdraftLimit();
        }

        // fallback
        return acc.getBalance();
    }

    // ---------- Operations ----------

    public void deposit(String accountNumber, double amount, String description) {
        getAccount(accountNumber).deposit(amount, description);
    }

    public void withdraw(String accountNumber, double amount, String description) {
        getAccount(accountNumber).withdraw(amount, description);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive");

        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);

        from.withdraw(amount, "Transfer to " + toAccountNumber + ": " + description);
        to.deposit(amount, "Transfer from " + fromAccountNumber + ": " + description);
    }

    // ---------- Helpers ----------

    private void ensureAccountDoesNotExist(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (accountsByNumber.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account already exists: " + accountNumber);
        }
    }
}
