import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 * Console entry point — menu-driven banking system.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final BankDAO dao = new BankDAO();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        deposit();
                        break;
                    case "3":
                        withdraw();
                        break;
                    case "4":
                        checkBalance();
                        break;
                    case "5":
                        transfer();
                        break;
                    case "6":
                        viewAllAccounts();
                        break;
                    case "7":
                        viewTransactionHistory();
                        break;
                    case "8":
                        running = false;
                        System.out.println("Thank you for using the Banking System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InsufficientBalanceException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n===== SIMPLE BANKING SYSTEM =====");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Transfer Funds");
        System.out.println("6. View All Accounts");
        System.out.println("7. View Transaction History");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createAccount() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Initial Deposit: ");
        double balance = readDouble();

        System.out.print("Enter Account Type (Savings/Current): ");
        String type = sc.nextLine().trim();

        Account acc = new Account(accNo, name, balance, type);
        dao.createAccount(acc);
        System.out.println("Account created successfully!\n" + acc);
    }

    private static void deposit() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();

        System.out.print("Enter Amount to Deposit: ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }

        dao.deposit(accNo, amount);
        System.out.println("Deposit successful. New balance: Rs. " +
                String.format("%.2f", dao.getAccount(accNo).getBalance()));
    }

    private static void withdraw() throws SQLException, InsufficientBalanceException {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();

        System.out.print("Enter Amount to Withdraw: ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        dao.withdraw(accNo, amount);
        System.out.println("Withdrawal successful. New balance: Rs. " +
                String.format("%.2f", dao.getAccount(accNo).getBalance()));
    }

    private static void checkBalance() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();

        Account acc = dao.getAccount(accNo);
        if (acc == null) {
            System.out.println("Account not found.");
        } else {
            System.out.println(acc);
        }
    }

    private static void transfer() throws SQLException, InsufficientBalanceException {
        System.out.print("Enter Sender Account Number: ");
        String from = sc.nextLine().trim();

        System.out.print("Enter Receiver Account Number: ");
        String to = sc.nextLine().trim();

        System.out.print("Enter Amount to Transfer: ");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }

        dao.transfer(from, to, amount);
        System.out.println("Transfer successful!");
    }

    private static void viewAllAccounts() throws SQLException {
        List<Account> accounts = dao.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("\n--- All Accounts ---");
            for (Account acc : accounts) {
                System.out.println(acc);
            }
        }
    }

    private static void viewTransactionHistory() throws SQLException {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();

        List<String> history = dao.getTransactionHistory(accNo);
        if (history.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            System.out.println("\n--- Transaction History for " + accNo + " ---");
            for (String txn : history) {
                System.out.println(txn);
            }
        }
    }

    // Helper: safely read a double, re-prompting on invalid input
    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }
}
