package model; // or whatever your main package is

import controller.BudgetController;
import view.BudgetGUI;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    BudgetController controller = new BudgetController(new BudgetManager());
    SwingUtilities.invokeLater(() -> new BudgetGUI(controller).setVisible(true));
  }
}
