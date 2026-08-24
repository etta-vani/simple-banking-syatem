/**
 * Account.java
 * Model class representing a bank account.
 */
public class Account {
    private String accountNumber;
    private String name;
    private double balance;
    private String accountType; // "Savings" or "Current"

    public Account(String accountNumber, String name, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.accountType = accountType;
    }

    // Getters and setters (encapsulation)
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        return "Account No: " + accountNumber +
                " | Name: " + name +
                " | Type: " + accountType +
                " | Balance: Rs. " + String.format("%.2f", balance);
    }
}
