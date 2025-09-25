package model;

public class Category {
  private String name;
  private double budgetLimit;

  public Category(String name, double budgetLimit) {
    this.name = name;
    this.budgetLimit = budgetLimit;
  }

  public String getName() {
    return name;
  }

  public double getBudgetLimit() {
    return budgetLimit;
  }

  public void setBudgetLimit(double budgetLimit) {
    this.budgetLimit = budgetLimit;
  }

  public void setName(String name) {
    this.name = name;
  }


  public boolean isOverLimit(double totalSpent) {
    return budgetLimit > 0 && totalSpent > budgetLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Category) {
      return this.name.equalsIgnoreCase(((Category) o).getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.toLowerCase().hashCode();
  }


}
