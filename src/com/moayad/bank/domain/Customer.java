package com.moayad.bank.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private final String customerId;
    private final String fullName;
    private final List<Account> accounts;

    public Customer(String customerId, String fullName) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }

        this.customerId = customerId;
        this.fullName = fullName;
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        //Prevent duplicates by account number
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(account.getAccountNumber())) {
                throw new IllegalArgumentException("Customer already has account: " + account.getAccountNumber());
            }
        }
        accounts.add(account);
    }

    public Account getAccountByNumber(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Customer does not have account: " + accountNumber);
    }

    @Override
    public String toString() {
        return customerId + " | " + fullName + " | accounts=" + accounts.size();
    }
}
