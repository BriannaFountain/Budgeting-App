package model;

public class Transaction {
  private double amount;
  private Category category;
  private boolean isIncome; // true = income, false = expense (change to is this income or expense)

  public Transaction(double amount, Category category, boolean isIncome) {
    this.amount = amount;
    this.category = category;
    this.isIncome = isIncome;
  }

  public double getAmount() {
    return amount;
  }

  public Category getCategory() {
    return category;
  }

  public boolean isIncome() {
    return isIncome;
  }

  // Convenience method for expenses
  public boolean isExpense() {
    return !isIncome;
  }
}
