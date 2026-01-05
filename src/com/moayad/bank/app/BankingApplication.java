package com.moayad.bank.app;

import com.moayad.bank.service.BankService;
import com.moayad.bank.ui.MainFrame;
import javax.swing.SwingUtilities;

public class BankingApplication {

    public static void main(String[] args) {
        BankService bankService = new BankService();

        SwingUtilities.invokeLater(() -> {
            new MainFrame(bankService).setVisible(true);
        });
    }
}

