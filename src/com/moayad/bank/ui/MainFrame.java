package com.moayad.bank.ui;

import com.moayad.bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final BankService bankService;

    private JTextArea logArea;

    // account info labels (Option A)
    private JLabel balanceLabel;
    private JLabel availableLabel;

    // track the last account the user interacted with
    private String lastAccountNumber = null;

    public MainFrame(BankService bankService) {
        this.bankService = bankService;

        setTitle("Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ===== TOP: Title + Account Info =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Banking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        //Account info panel under title
        JPanel accountInfoPanel = new JPanel(new GridLayout(2, 1));
        accountInfoPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        balanceLabel = new JLabel("Balance: $0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        availableLabel = new JLabel("Available: $0.00");
        availableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        accountInfoPanel.add(balanceLabel);
        accountInfoPanel.add(availableLabel);

        topPanel.add(accountInfoPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: Buttons =====
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton createCustomerBtn = new JButton("Create Customer");
        JButton openSavingsBtn = new JButton("Open Savings");
        JButton openChequingBtn = new JButton("Open Chequing");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton transferBtn = new JButton("Transfer");

        buttonsPanel.add(createCustomerBtn);
        buttonsPanel.add(openSavingsBtn);
        buttonsPanel.add(openChequingBtn);
        buttonsPanel.add(depositBtn);
        buttonsPanel.add(withdrawBtn);
        buttonsPanel.add(transferBtn);

        add(buttonsPanel, BorderLayout.CENTER);

        // ===== BOTTOM: Log =====
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Activity Log"));
        scrollPane.setPreferredSize(new Dimension(800, 180));

        add(scrollPane, BorderLayout.SOUTH);

        // ===== Button Actions =====

        createCustomerBtn.addActionListener(e -> {
            String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID (e.g., C001):");
            if (customerId == null) return;
            customerId = customerId.trim();
            if (customerId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer ID cannot be empty.");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            if (name == null) return;
            name = name.trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name cannot be empty.");
                return;
            }

            try {
                bankService.createCustomer(customerId, name);
                log("✅ Customer created: " + customerId + " | " + name);
                JOptionPane.showMessageDialog(this, "Customer created successfully!");
            } catch (Exception ex) {
                log("❌ Failed to create customer: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        openSavingsBtn.addActionListener(e -> {
            String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID:");
            if (customerId == null || customerId.trim().isEmpty()) return;

            String accountNumber = JOptionPane.showInputDialog(this, "Enter Savings Account Number (e.g., SAV-001):");
            if (accountNumber == null || accountNumber.trim().isEmpty()) return;

            String rateStr = JOptionPane.showInputDialog(this, "Enter Interest Rate (e.g., 0.02):");
            if (rateStr == null || rateStr.trim().isEmpty()) return;

            try {
                double interestRate = Double.parseDouble(rateStr.trim());

                bankService.openSavingsAccount(
                        customerId.trim(),
                        accountNumber.trim(),
                        interestRate
                );

                log("✅ Savings account opened: " + accountNumber.trim() + " (Rate: " + interestRate + ")");
                JOptionPane.showMessageDialog(this, "Savings account opened successfully!");

                // ✅ NEW: update info for this account
                setLastAccount(accountNumber.trim());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid interest rate.");
            } catch (Exception ex) {
                log("❌ Failed to open savings: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        openChequingBtn.addActionListener(e -> {
            log("Open Chequing clicked");

            try {
                String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID (e.g., C001):");
                if (customerId == null || customerId.trim().isEmpty()) return;

                String accountNumber = JOptionPane.showInputDialog(this, "Enter Chequing Account Number (e.g., CHQ-10001):");
                if (accountNumber == null || accountNumber.trim().isEmpty()) return;

                String overdraftStr = JOptionPane.showInputDialog(this, "Enter Overdraft Limit (e.g., 500):");
                if (overdraftStr == null || overdraftStr.trim().isEmpty()) return;

                double overdraftLimit = Double.parseDouble(overdraftStr.trim());

                bankService.openChequingAccount(customerId.trim(), accountNumber.trim(), overdraftLimit);

                log("✅ Chequing account opened: " + accountNumber.trim() + " (Overdraft: " + overdraftLimit + ")");
                JOptionPane.showMessageDialog(this, "Chequing account opened successfully!");

                //update info for this account
                setLastAccount(accountNumber.trim());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid overdraft limit. Please enter a number.");
                log("❌ Failed to open chequing: invalid overdraft input");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                log("❌ Failed to open chequing: " + ex.getMessage());
            }
        });

        depositBtn.addActionListener(e -> {
            log("Deposit clicked");

            try {
                String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number (e.g., SAV-10001 or CHQ-10001):");
                if (accountNumber == null || accountNumber.trim().isEmpty()) return;

                String amountStr = JOptionPane.showInputDialog(this, "Enter Deposit Amount (e.g., 100):");
                if (amountStr == null || amountStr.trim().isEmpty()) return;

                String description = JOptionPane.showInputDialog(this, "Enter Description (e.g., Paycheck):");
                if (description == null || description.trim().isEmpty()) description = "Deposit";

                double amount = Double.parseDouble(amountStr.trim());

                bankService.deposit(accountNumber.trim(), amount, description.trim());

                log("✅ Deposit successful: " + accountNumber.trim() + " | $" + amount + " | " + description.trim());
                JOptionPane.showMessageDialog(this, "Deposit completed successfully!");

                //update labels
                setLastAccount(accountNumber.trim());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
                log("❌ Deposit failed: invalid amount input");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                log("❌ Deposit failed: " + ex.getMessage());
            }
        });

        withdrawBtn.addActionListener(e -> {
            log("Withdraw clicked");

            try {
                String accountNumber = JOptionPane.showInputDialog(this, "Enter Account Number (e.g., SAV-10001 or CHQ-10001):");
                if (accountNumber == null || accountNumber.trim().isEmpty()) return;

                String amountStr = JOptionPane.showInputDialog(this, "Enter Withdraw Amount (e.g., 50):");
                if (amountStr == null || amountStr.trim().isEmpty()) return;

                String description = JOptionPane.showInputDialog(this, "Enter Description (e.g., Groceries):");
                if (description == null || description.trim().isEmpty()) description = "Withdraw";

                double amount = Double.parseDouble(amountStr.trim());

                bankService.withdraw(accountNumber.trim(), amount, description.trim());

                log("✅ Withdraw successful: " + accountNumber.trim() + " | $" + amount + " | " + description.trim());
                JOptionPane.showMessageDialog(this, "Withdrawal completed successfully!");

                //update labels
                setLastAccount(accountNumber.trim());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
                log("❌ Withdraw failed: invalid amount input");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                log("❌ Withdraw failed: " + ex.getMessage());
            }
        });

        transferBtn.addActionListener(e -> {
            log("Transfer clicked");

            try {
                String fromAccount = JOptionPane.showInputDialog(this, "From Account Number (e.g., CHQ-10001):");
                if (fromAccount == null || fromAccount.trim().isEmpty()) return;

                String toAccount = JOptionPane.showInputDialog(this, "To Account Number (e.g., SAV-10001):");
                if (toAccount == null || toAccount.trim().isEmpty()) return;

                String amountStr = JOptionPane.showInputDialog(this, "Enter Transfer Amount (e.g., 25):");
                if (amountStr == null || amountStr.trim().isEmpty()) return;

                String description = JOptionPane.showInputDialog(this, "Enter Description (e.g., Savings transfer):");
                if (description == null || description.trim().isEmpty()) description = "Transfer";

                double amount = Double.parseDouble(amountStr.trim());

                bankService.transfer(fromAccount.trim(), toAccount.trim(), amount, description.trim());

                log("✅ Transfer successful: " + fromAccount.trim() + " → " + toAccount.trim()
                        + " | $" + amount + " | " + description.trim());
                JOptionPane.showMessageDialog(this, "Transfer completed successfully!");

                //update labels (show the "from" account by default)
                setLastAccount(fromAccount.trim());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
                log("❌ Transfer failed: invalid amount input");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                log("❌ Transfer failed: " + ex.getMessage());
            }
        });

        refreshLabels();
    }

    // updates lastAccountNumber and refreshes labels
    private void setLastAccount(String accountNumber) {
        this.lastAccountNumber = accountNumber;
        refreshLabels();
    }

    // refresh UI labels from BankService
    private void refreshLabels() {
        if (lastAccountNumber == null || lastAccountNumber.trim().isEmpty()) {
            balanceLabel.setText("Balance: $0.00");
            availableLabel.setText("Available: $0.00");
            return;
        }

        try {
            double balance = bankService.getBalance(lastAccountNumber);
            double available = bankService.getAvailableFunds(lastAccountNumber);

            balanceLabel.setText(String.format("Balance (%s): $%.2f", lastAccountNumber, balance));
            availableLabel.setText(String.format("Available: $%.2f", available));
        } catch (Exception e) {
            balanceLabel.setText("Balance: —");
            availableLabel.setText("Available: —");
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}