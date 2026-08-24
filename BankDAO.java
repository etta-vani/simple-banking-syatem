import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * BankDAO.java
 * Data Access Object — all database operations for accounts and transactions.
 * Uses PreparedStatement everywhere (prevents SQL injection — good talking point).
 */
public class BankDAO {

    // ---------- CREATE ACCOUNT ----------
    public void createAccount(Account acc) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, name, balance, account_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getName());
            ps.setDouble(3, acc.getBalance());
            ps.setString(4, acc.getAccountType());
            ps.executeUpdate();
        }
    }

    // ---------- FETCH SINGLE ACCOUNT ----------
    public Account getAccount(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getString("account_number"),
                            rs.getString("name"),
                            rs.getDouble("balance"),
                            rs.getString("account_type")
                    );
                }
            }
        }
        return null; // not found
    }

    // ---------- VIEW ALL ACCOUNTS ----------
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getString("account_number"),
                        rs.getString("name"),
                        rs.getDouble("balance"),
                        rs.getString("account_type")
                ));
            }
        }
        return accounts;
    }

    // ---------- DEPOSIT ----------
    public void deposit(String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, accountNumber);
            ps.executeUpdate();
        }
        logTransaction(accountNumber, "DEPOSIT", amount);
    }

    // ---------- WITHDRAW ----------
    public void withdraw(String accountNumber, double amount) throws SQLException, InsufficientBalanceException {
        Account acc = getAccount(accountNumber);
        if (acc == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        if (acc.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: Rs. " + String.format("%.2f", acc.getBalance()));
        }

        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, accountNumber);
            ps.executeUpdate();
        }
        logTransaction(accountNumber, "WITHDRAW", amount);
    }

    // ---------- TRANSFER (two accounts, one operation) ----------
    public void transfer(String fromAccount, String toAccount, double amount)
            throws SQLException, InsufficientBalanceException {

        Account from = getAccount(fromAccount);
        Account to = getAccount(toAccount);

        if (from == null || to == null) {
            throw new IllegalArgumentException("One or both account numbers do not exist.");
        }
        if (from.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for transfer. Available: Rs. " + String.format("%.2f", from.getBalance()));
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // manual transaction control

            String deductSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(deductSql)) {
                ps.setDouble(1, amount);
                ps.setString(2, fromAccount);
                ps.executeUpdate();
            }

            String addSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(addSql)) {
                ps.setDouble(1, amount);
                ps.setString(2, toAccount);
                ps.executeUpdate();
            }

            conn.commit(); // both succeed together
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // undo both if either fails
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        logTransaction(fromAccount, "TRANSFER_OUT to " + toAccount, amount);
        logTransaction(toAccount, "TRANSFER_IN from " + fromAccount, amount);
    }

    // ---------- TRANSACTION LOGGING ----------
    private void logTransaction(String accountNumber, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, type, amount, txn_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    // ---------- TRANSACTION HISTORY ----------
    public List<String> getTransactionHistory(String accountNumber) throws SQLException {
        List<String> history = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY txn_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    history.add(String.format("[%s] %s : Rs. %.2f",
                            rs.getTimestamp("txn_date"),
                            rs.getString("type"),
                            rs.getDouble("amount")));
                }
            }
        }
        return history;
    }
}
