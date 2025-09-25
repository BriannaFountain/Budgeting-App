package view;

import controller.BudgetController;
import model.Account;
import model.Category;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BudgetGUI extends JFrame {
  private BudgetController controller;
  private Account currentAccount;

  private CardLayout cardLayout;
  private JPanel mainPanel;

  // Panels
  private JPanel loginPanel, dashboardPanel, transactionPanel, categoryPanel, summaryPanel, transactionsPanel;

  public BudgetGUI(BudgetController controller) {
    this.controller = controller;
    setTitle("Budget App");
    setSize(600, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);
    add(mainPanel);

    initLoginPanel();
    initDashboardPanel();
    initTransactionPanel();
    initCategoryPanel();
    initSummaryPanel();
    initTransactionsPanel();

    mainPanel.add(loginPanel, "Login");
    mainPanel.add(dashboardPanel, "Dashboard");
    mainPanel.add(transactionPanel, "Transaction");
    mainPanel.add(categoryPanel, "Category");
    mainPanel.add(summaryPanel, "Summary");
    mainPanel.add(transactionsPanel, "Transactions");

    cardLayout.show(mainPanel, "Login");
  }

  private void initLoginPanel() {
    loginPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    JLabel userLabel = new JLabel("Username:");
    JTextField userField = new JTextField(15);
    JLabel passLabel = new JLabel("Password:");
    JPasswordField passField = new JPasswordField(15);

    JButton loginBtn = new JButton("Login");
    JButton createBtn = new JButton("Create Account");

    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0; c.gridy = 0; loginPanel.add(userLabel, c);
    c.gridx = 1; loginPanel.add(userField, c);
    c.gridx = 0; c.gridy = 1; loginPanel.add(passLabel, c);
    c.gridx = 1; loginPanel.add(passField, c);
    c.gridx = 0; c.gridy = 2; loginPanel.add(loginBtn, c);
    c.gridx = 1; loginPanel.add(createBtn, c);

    loginBtn.addActionListener(e -> {
      String username = userField.getText();
      String password = new String(passField.getPassword());
      Account acc = controller.login(username, password);
      if (acc != null) {
        currentAccount = acc;
        updateDashboard();
        cardLayout.show(mainPanel, "Dashboard");
      } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    createBtn.addActionListener(e -> {
      String username = userField.getText();
      String password = new String(passField.getPassword());
      if (controller.createAccount(username, password)) {
        JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  private void initDashboardPanel() {
    dashboardPanel = new JPanel();
    dashboardPanel.setLayout(new BorderLayout());

    JLabel balanceLabel = new JLabel();
    balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
    balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));

    JPanel buttonPanel = new JPanel();
    JButton addTxnBtn = new JButton("Add Transaction");
    JButton viewTxnsBtn = new JButton("View Transactions");
    JButton addCatBtn = new JButton("Add Category");
    JButton summaryBtn = new JButton("Summary Report");
    JButton logoutBtn = new JButton("Logout");

    buttonPanel.add(addTxnBtn);
    buttonPanel.add(viewTxnsBtn);
    buttonPanel.add(addCatBtn);
    buttonPanel.add(summaryBtn);
    buttonPanel.add(logoutBtn);

    dashboardPanel.add(balanceLabel, BorderLayout.CENTER);
    dashboardPanel.add(buttonPanel, BorderLayout.SOUTH);

    addTxnBtn.addActionListener(e -> cardLayout.show(mainPanel, "Transaction"));
    viewTxnsBtn.addActionListener(e -> {
      updateTransactionsTable();
      cardLayout.show(mainPanel, "Transactions");
    });
    addCatBtn.addActionListener(e -> cardLayout.show(mainPanel, "Category"));
    summaryBtn.addActionListener(e -> {
      updateSummary();
      cardLayout.show(mainPanel, "Summary");
    });
    logoutBtn.addActionListener(e -> {
      currentAccount = null;
      cardLayout.show(mainPanel, "Login");
    });

    // Update dashboard balance when shown
    dashboardPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        balanceLabel.setText("Balance: $" + controller.getBalance(currentAccount));
      }
    });
  }

  private void initTransactionPanel() {
    transactionPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    JLabel catLabel = new JLabel("Category:");
    JTextField catField = new JTextField(15);
    JLabel amtLabel = new JLabel("Amount:");
    JTextField amtField = new JTextField(15);
    JLabel typeLabel = new JLabel("Type:");
    String[] types = {"Income", "Expense"};
    JComboBox<String> typeBox = new JComboBox<>(types);

    JButton addBtn = new JButton("Add");
    JButton backBtn = new JButton("Back");

    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0; c.gridy = 0; transactionPanel.add(catLabel, c);
    c.gridx = 1; transactionPanel.add(catField, c);
    c.gridx = 0; c.gridy = 1; transactionPanel.add(amtLabel, c);
    c.gridx = 1; transactionPanel.add(amtField, c);
    c.gridx = 0; c.gridy = 2; transactionPanel.add(typeLabel, c);
    c.gridx = 1; transactionPanel.add(typeBox, c);
    c.gridx = 0; c.gridy = 3; transactionPanel.add(addBtn, c);
    c.gridx = 1; transactionPanel.add(backBtn, c);

    addBtn.addActionListener((ActionEvent e) -> {
      String catName = catField.getText();
      double amt = Double.parseDouble(amtField.getText());
      boolean isIncome = typeBox.getSelectedItem().equals("Income");

      controller.addTransaction(currentAccount, catName, amt, isIncome);

      JOptionPane.showMessageDialog(this, "Transaction added!");
      catField.setText("");
      amtField.setText("");
    });

    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
  }

  private JTable transactionsTable;
  private void initTransactionsPanel() {
    transactionsPanel = new JPanel(new BorderLayout());
    transactionsTable = new JTable();
    JScrollPane scrollPane = new JScrollPane(transactionsTable);
    transactionsPanel.add(scrollPane, BorderLayout.CENTER);

    JButton backBtn = new JButton("Back");
    transactionsPanel.add(backBtn, BorderLayout.SOUTH);
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
  }

  private void updateTransactionsTable() {
    String[] columns = {"Category", "Amount", "Type"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);
    for (Transaction t : controller.getTransactions(currentAccount)) {
      String type = t.isIncome() ? "Income" : "Expense";
      model.addRow(new Object[]{t.getCategory().getName(), t.getAmount(), type});
    }
    transactionsTable.setModel(model);
  }

  private JTextArea summaryArea;
  private void initSummaryPanel() {
    summaryPanel = new JPanel(new BorderLayout());
    summaryArea = new JTextArea();
    summaryArea.setEditable(false);
    summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

    JButton backBtn = new JButton("Back");
    summaryPanel.add(backBtn, BorderLayout.SOUTH);
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
  }

  private void updateSummary() {
    summaryArea.setText(controller.summaryReport(currentAccount));
  }

  private JTextField catNameField, catLimitField;
  private void initCategoryPanel() {
    categoryPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    JLabel nameLabel = new JLabel("Category Name:");
    catNameField = new JTextField(15);
    JLabel limitLabel = new JLabel("Limit (0 = no limit):");
    catLimitField = new JTextField(15);

    JButton addBtn = new JButton("Add");
    JButton backBtn = new JButton("Back");

    c.insets = new Insets(5,5,5,5);
    c.gridx = 0; c.gridy = 0; categoryPanel.add(nameLabel, c);
    c.gridx = 1; categoryPanel.add(catNameField, c);
    c.gridx = 0; c.gridy = 1; categoryPanel.add(limitLabel, c);
    c.gridx = 1; categoryPanel.add(catLimitField, c);
    c.gridx = 0; c.gridy = 2; categoryPanel.add(addBtn, c);
    c.gridx = 1; categoryPanel.add(backBtn, c);

    addBtn.addActionListener(e -> {
      String name = catNameField.getText();
      double limit = Double.parseDouble(catLimitField.getText());
      currentAccount.getProfile().addCategory(new Category(name, limit));
      JOptionPane.showMessageDialog(this, "Category added!");
      catNameField.setText("");
      catLimitField.setText("");
    });

    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
  }

  private void updateDashboard() {
    // Trigger componentShown to update balance
    dashboardPanel.dispatchEvent(new java.awt.event.ComponentEvent(dashboardPanel, java.awt.event.ComponentEvent.COMPONENT_SHOWN));
  }

  public static void main(String[] args) {
    BudgetController controller = new BudgetController(new model.BudgetManager());
    SwingUtilities.invokeLater(() -> new BudgetGUI(controller).setVisible(true));
  }
}
