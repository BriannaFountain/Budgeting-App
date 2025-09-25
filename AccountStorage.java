package model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountStorage {
  private static final String SAVE_FILE = "accounts.txt";

  // Save all accounts and their transactions
  public void save(Map<String, Account> accounts) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
      for (Account acc : accounts.values()) {
        StringBuilder line = new StringBuilder();
        // username and password
        line.append(acc.getUsername()).append(":").append(acc.getPassword());

        // append transactions if any
        if (!acc.getProfile().getTransactions().isEmpty()) {
          line.append("|"); // separate account info from transactions
          for (int i = 0; i < acc.getProfile().getTransactions().size(); i++) {
            Transaction t = acc.getProfile().getTransactions().get(i);
            line.append(t.getCategory().getName())
                    .append(":")
                    .append(t.getAmount())
                    .append(":")
                    .append(t.isIncome() ? "I" : "E");
            if (i < acc.getProfile().getTransactions().size() - 1) {
              line.append(","); // separate transactions
            }
          }
        }
        pw.println(line.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Load all accounts and their transactions
  public Map<String, Account> load() {
    Map<String, Account> accounts = new HashMap<>();
    File file = new File(SAVE_FILE);
    if (!file.exists()) return accounts;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split("\\|");
        String[] userParts = parts[0].split(":");
        if (userParts.length != 2) continue;

        Account acc = new Account(userParts[0], userParts[1], new Profile(userParts[0]));

        // Load transactions if present
        if (parts.length == 2) {
          String[] txns = parts[1].split(",");
          for (String t : txns) {
            String[] txnParts = t.split(":");
            if (txnParts.length != 3) continue;
            String catName = txnParts[0];
            double amt = Double.parseDouble(txnParts[1]);
            boolean isIncome = txnParts[2].equals("I");
            Category c = new Category(catName, 0); // default limit
            acc.getProfile().addTransaction(new Transaction(amt, c, isIncome));
          }
        }

        accounts.put(acc.getUsername(), acc);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return accounts;
  }
}
