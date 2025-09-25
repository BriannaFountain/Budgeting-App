package controller;

import model.*;

import java.util.List;

public class BudgetController {
  private BudgetManager manager;
  private Account currentUser;

  public BudgetController(BudgetManager manager) {
    this.manager = manager;
  }

  // Account operations
  public boolean createAccount(String username, String password) {
    return manager.createAccount(username, password);
  }

  public Account login(String username, String password) {
    return manager.login(username, password);
  }

  public void deleteAccount(String username) {
    manager.deleteAccount(username);
  }

  // Transaction operations
  public void addTransaction(Account acc, String category, double amount, boolean isIncome) {
    Category c = new Category(category, 0); // default limit
    acc.getProfile().addTransaction(new Transaction(amount, c, isIncome));
    manager.save(); // persist
  }

  public List<Transaction> getTransactions(Account acc) {
    return acc.getProfile().getTransactions();
  }

  public double getBalance(Account acc) {
    return acc.getProfile().getBalance();
  }

  public double getExpensesByCategory(Account acc, Category c) {
    return acc.getProfile().getExpensesByCategory(c);
  }

  // Summary report
  public String summaryReport(Account acc) {
    Profile p = acc.getProfile();
    StringBuilder sb = new StringBuilder();
    sb.append("Summary Report for ").append(p.getName()).append("\n");

    double totalIncome = 0;
    double totalExpenses = 0;

    for (Transaction t : p.getTransactions()) {
      if (t.isIncome()) totalIncome += t.getAmount();
      else totalExpenses += t.getAmount();
    }

    sb.append("Total Income: $").append(totalIncome).append("\n");
    sb.append("Total Expenses: $").append(totalExpenses).append("\n");
    sb.append("Net Balance: $").append(p.getBalance()).append("\n\n");

    sb.append("Expenses by Category:\n");
    for (Category cat : p.getCategories()) {
      double spent = p.getExpensesByCategory(cat);
      sb.append("- ").append(cat.getName()).append(": $").append(spent).append("\n");
    }

    return sb.toString();
  }



  public void login(Account user) {
    this.currentUser = user;
  }

  public void logout() {
    this.currentUser = null;
    System.out.println("You have been logged out.");
  }

  public boolean isLoggedIn() {
    return this.currentUser != null;
  }
}
