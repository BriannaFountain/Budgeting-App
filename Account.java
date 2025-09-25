package model;

public class Account {
  private String username;
  private String password;  // (for now plain text, later you can hash)
  private Profile profile;

  public Account(String username, String password, Profile profile) {
    this.username = username;
    this.password = password;
    this.profile = profile;
  }

  public String getUsername() {
    return username;
  }

  public boolean checkPassword(String input) {
    return this.password.equals(input);
  }

  public Profile getProfile() {
    return profile;
  }

  public String getPassword() {
    return password;
  }
}
