package model;

import java.util.ArrayList;

public class Profile {
  private String name;
  private ArrayList<Transaction> transactions;
  private ArrayList<Category> categories;

  public Profile(String name) {
    this.name = name;
    this.transactions = new ArrayList<>();
    this.categories = new ArrayList<>();
  }

  public void addTransaction(Transaction t) {
    transactions.add(t);
  }

  public double getBalance() {
    double total = 0;
    for (Transaction t : transactions) {
      if (t.isIncome()) {
        total += t.getAmount();
      } else {
        total -= t.getAmount();
      }
    }
    return total;
  }


  public String getName() {
    return name;
  }

  public ArrayList<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public void addCategory(Category c) {
    categories.add(c);
  }

  public void removeCategory(Category c) {
    categories.remove(c);
  }

  public ArrayList<Category> getCategories() {
    return new ArrayList<>(categories);
  }

  /*
  public double getExpensesByCategory(Category c) {
    return transactions.stream()
            .filter(t -> t.getCategory().equals(c) && t.isExpense())
            .mapToDouble(Transaction::getAmount)
            .sum();
  }

   */

  public double getExpensesByCategory(Category c) {
    double total = 0;
    for (Transaction t : transactions) {
      if (t.getCategory().equals(c)) {
        total += t.getAmount();
      }
    }
    return total;
  }

  public double getIncomeByCategory(Category c) {
    double total = 0;
    for (Transaction t : transactions) {
      if (t.getCategory().equals(c)) {
        total += t.getAmount();
      }
    }
    return total;
  }
}
