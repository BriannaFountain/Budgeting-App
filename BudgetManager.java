package model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BudgetManager {
  private Map<String, Account> accounts;
  private static final String SAVE_FILE = "accounts.txt";

  public BudgetManager() {
    this.accounts = new HashMap<>();
    loadAccounts();
  }

  // Create a new account
  public boolean createAccount(String username, String password) {
    if (accounts.containsKey(username)) return false;
    Account acc = new Account(username, password, new Profile(username));
    accounts.put(username, acc);
    saveAccounts();
    return true;
  }

  // Login
  public Account login(String username, String password) {
    Account acc = accounts.get(username);
    if (acc != null && acc.checkPassword(password)) return acc;
    return null;
  }

  // Delete account
  public void deleteAccount(String username) {
    accounts.remove(username);
    saveAccounts();
  }

  // Save all accounts to disk
  public void saveAccounts() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
      for (Account acc : accounts.values()) {
        pw.println(acc.getUsername() + ";" + acc.getPassword() + ";" + serializeProfile(acc.getProfile()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Load all accounts from disk
  public void loadAccounts() {
    File f = new File(SAVE_FILE);
    if (!f.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(";", 3);
        if (parts.length == 3) {
          String username = parts[0];
          String password = parts[1];
          Profile profile = deserializeProfile(parts[2]);
          accounts.put(username, new Account(username, password, profile));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Convert Profile to a savable string
  private String serializeProfile(Profile p) {
    StringBuilder sb = new StringBuilder();

    // Categories: name|limit,name|limit,...
    for (Category c : p.getCategories()) {
      sb.append(c.getName()).append("|").append(c.getBudgetLimit()).append(",");
    }
    sb.append(";"); // separator for categories and transactions

    // Transactions: amount|category|income,amount|category|income,...
    for (Transaction t : p.getTransactions()) {
      sb.append(t.getAmount()).append("|")
              .append(t.getCategory().getName()).append("|")
              .append(t.isIncome()).append(",");
    }

    return sb.toString();
  }

  // Convert string back to Profile
  private Profile deserializeProfile(String data) {
    String[] parts = data.split(";", 2);
    Profile p = new Profile("user"); // placeholder name

    // Categories
    if (parts.length > 0 && !parts[0].isEmpty()) {
      String[] catParts = parts[0].split(",");
      for (String cStr : catParts) {
        if (!cStr.isEmpty()) {
          String[] cSplit = cStr.split("\\|");
          p.addCategory(new Category(cSplit[0], Double.parseDouble(cSplit[1])));
        }
      }
    }

    // Transactions
    if (parts.length > 1 && !parts[1].isEmpty()) {
      String[] txnParts = parts[1].split(",");
      for (String tStr : txnParts) {
        if (!tStr.isEmpty()) {
          String[] tSplit = tStr.split("\\|");
          double amt = Double.parseDouble(tSplit[0]);
          String catName = tSplit[1];
          boolean income = Boolean.parseBoolean(tSplit[2]);

          // find or create category
          Category cat = p.getCategories().stream()
                  .filter(c -> c.getName().equalsIgnoreCase(catName))
                  .findFirst()
                  .orElseGet(() -> {
                    Category newCat = new Category(catName, 0);
                    p.addCategory(newCat);
                    return newCat;
                  });

          p.addTransaction(new Transaction(amt, cat, income));
        }
      }
    }
    return p;
  }

  // Save wrapper
  public void save() {
    saveAccounts();
  }
}
