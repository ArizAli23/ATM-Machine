package ATMSimulation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ATMsGUI extends JFrame {
    Transaction t1 = new Transaction(1001, "Ariz Ali", 1154, 50000);
    Transaction t2 = new Transaction(1002, "Eshaan", 1150, 5000);
    Transaction t3 = new Transaction(1003, "Ali Rasikh", 1153, 10000);
    Transaction t4 = new Transaction(1004, "Aashesh", 1161, 15000);

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final Color baseColor = new Color(204, 255, 204);
    private JLabel balanceLabel;
    private final List<JTextField> inputFields = new ArrayList<>();

    public ATMsGUI() throws IOException {
        setTitle("ATM Machine");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(baseColor);

        cardPanel.add(createHomeScreen(), "home");
        cardPanel.add(createPinScreen("Enter PIN to Deposit:", "depositAmount"), "depositPin");
        cardPanel.add(createAmountScreen("Enter Amount to Deposit", "deposit", "home"), "depositAmount");
        cardPanel.add(createPinScreen("Enter PIN to Withdraw:", "withdrawAmount"), "withdrawPin");
        cardPanel.add(createAmountScreen("Enter Amount to Withdraw", "withdraw", "home"), "withdrawAmount");
        cardPanel.add(createPinScreen("Enter PIN to Transfer:", "transferDetails"), "transferPin");
        cardPanel.add(createTransferDetailsScreen(), "transferDetails");
        cardPanel.add(createPinScreen("Enter PIN to Check Balance:", "showBalance"), "checkBalancePin");
        cardPanel.add(createBalanceScreen(), "showBalance");
        cardPanel.add(createPinScreen("Enter Current PIN:", "newPin"), "changePinOld");
        cardPanel.add(createNewPinScreen(), "newPin");

        add(cardPanel);
        setVisible(true);
    }

    private void clearAllFields() {
        for (JTextField field : inputFields) {
            field.setText("");
        }
    }

    private JButton createHomeButton() {
        JButton home = createButton("HOME");
        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.addActionListener(e -> {
            clearAllFields();
            cardLayout.show(cardPanel, "home");
        });
        return home;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(144, 238, 144));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton deposit = createButton("Deposit Cash");
        JButton withdraw = createButton("Withdraw Money");
        JButton transfer = createButton("Transfer Cash");
        JButton check = createButton("Check Balance");
        JButton changePin = createButton("Change PIN");
        JButton exit = createButton("Exit");

        panel.add(deposit);
        panel.add(withdraw);
        panel.add(transfer);
        panel.add(check);
        panel.add(changePin);
        panel.add(exit);

        deposit.addActionListener(e -> cardLayout.show(cardPanel, "depositPin"));
        withdraw.addActionListener(e -> cardLayout.show(cardPanel, "withdrawPin"));
        transfer.addActionListener(e -> cardLayout.show(cardPanel, "transferPin"));
        check.addActionListener(e -> cardLayout.show(cardPanel, "checkBalancePin"));
        changePin.addActionListener(e -> cardLayout.show(cardPanel, "changePinOld"));
        exit.addActionListener(e -> System.exit(0));

        return panel;
    }

    private JPanel createPinScreen(String labelText, String nextCard) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField pinField = new JTextField(10);
        pinField.setMaximumSize(new Dimension(200, 30));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputFields.add(pinField);

        JButton enter = createButton("Enter");
        enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.addActionListener(e -> {
            try {
                int PIN = Integer.parseInt(pinField.getText().trim());
                t1.checkPIN(PIN);
                if (nextCard.equals("showBalance")) {
                    balanceLabel.setText("Your Balance is: $" + t1.checkBalance());
                }
                cardLayout.show(cardPanel, nextCard);
            } catch (InvalidPINException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pinField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(enter);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createHomeButton());

        return panel;
    }

    private JPanel createAmountScreen(String labelText, String operationType, String backTo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField amountField = new JTextField(10);
        amountField.setMaximumSize(new Dimension(200, 30));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputFields.add(amountField);

        JButton confirm = createButton("Confirm");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirm.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText().trim());
                if (operationType.equalsIgnoreCase("deposit")) {
                    t1.deposit(amount);
                    filingTransaction(amount, "Deposit");
                    JOptionPane.showMessageDialog(this, "Deposit Successful");
                } else if (operationType.equalsIgnoreCase("withdraw")) {
                    t1.withdraw(amount);
                    filingTransaction(amount, "Withdraw");
                    JOptionPane.showMessageDialog(this, "Withdrawal Successful");
                }
                clearAllFields();
                cardLayout.show(cardPanel, "home");
            } catch (InsufficientFundsException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(confirm);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createHomeButton());

        return panel;
    }

    private JPanel createTransferDetailsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel accLabel = new JLabel("Enter Account ID:");
        JTextField accField = new JTextField(10);
        accField.setMaximumSize(new Dimension(200, 30));
        inputFields.add(accField);

        JLabel amountLabel = new JLabel("Enter Amount:");
        JTextField amountField = new JTextField(10);
        amountField.setMaximumSize(new Dimension(200, 30));
        inputFields.add(amountField);

        JButton transfer = createButton("Transfer");
        JButton home = createButton("HOME");

        accLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accField.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        transfer.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        transfer.addActionListener(e -> {
            try {
                int accountID = Integer.parseInt(accField.getText().trim());
                int amount = Integer.parseInt(amountField.getText().trim());
                t1.searchAccount(accountID);
                t1.transfer(amount, accountID);
                filingTransaction(amount, "Transfer (" + accountID + ")");
                JOptionPane.showMessageDialog(this, "Successful Transfer of $" + amount + " to " + accountID);
                clearAllFields();
                cardLayout.show(cardPanel, "home");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(accLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(accField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(amountLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(transfer);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createHomeButton());

        return panel;
    }

    private JPanel createBalanceScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(balanceLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(createHomeButton());

        return panel;
    }

    private JPanel createNewPinScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));

        JLabel label = new JLabel("Enter New PIN:");
        JTextField newPinField = new JTextField(10);
        newPinField.setMaximumSize(new Dimension(200, 30));
        inputFields.add(newPinField);

        JButton save = createButton("Save");
        JButton home = createButton("HOME");

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPinField.setAlignmentX(Component.CENTER_ALIGNMENT);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);

        save.addActionListener(e -> {
            int newPIN = Integer.parseInt(newPinField.getText().trim());
            t1.setAccountPIN(newPIN);
            JOptionPane.showMessageDialog(this, "PIN Changed Successfully!");
            clearAllFields();
            cardLayout.show(cardPanel, "home");
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(newPinField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(save);
        panel.add(Box.createVerticalStrut(10));
        panel.add(createHomeButton());

        return panel;
    }

    public void filingTransaction(int amount, String type) throws IOException {
        File file = new File("Transaction History.txt");

        boolean fileExists = file.exists();

        try (FileWriter fw = new FileWriter(file, true)) {
            if (!fileExists) {
                fw.write("Transaction History\n");
                fw.write("Amount\tType\t\t\t\tDate\t\tTime\n");
            }

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now().withNano(0);
            fw.write(amount + "\t" + type + "\t\t\t\t" + date + "\t" + time + "\n");
        }
    }


    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new ATMsGUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
