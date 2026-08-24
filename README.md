# Simple Banking System (Java + JDBC + MySQL)

A console-based banking application demonstrating core Java, JDBC, and MySQL integration.

## Features
- Create Account
- Deposit Money
- Withdraw Money (with balance validation)
- Check Balance
- Transfer Funds between accounts (transactional — commit/rollback)
- View All Accounts
- View Transaction History

## Tech Used
- Core Java (OOP, exception handling, collections)
- JDBC (PreparedStatement, transactions)
- MySQL

## Files
| File | Purpose |
|---|---|
| `Account.java` | Model class for a bank account |
| `InsufficientBalanceException.java` | Custom exception for failed withdrawals/transfers |
| `DBConnection.java` | JDBC connection setup |
| `BankDAO.java` | All database operations (CRUD, transfer, transaction log) |
| `Main.java` | Console menu / entry point |
| `schema.sql` | MySQL table creation script |

## Setup Instructions

### 1. Install MySQL (if not already installed)
Make sure MySQL server is running locally.

### 2. Create the database and tables
Open MySQL and run:
```bash
mysql -u root -p < schema.sql
```
Or copy-paste the contents of `schema.sql` into MySQL Workbench / CLI.

### 3. Download the MySQL JDBC Driver
Download `mysql-connector-j-<version>.jar` from:
https://dev.mysql.com/downloads/connector/j/

### 4. Update DB credentials
In `DBConnection.java`, change:
```java
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "yourpassword"; // <-- set your actual MySQL password
```

### 5. Compile and run

**If using plain javac/java (command line):**
```bash
# Compile
javac -cp .:mysql-connector-j-<version>.jar *.java

# Run (Linux/Mac)
java -cp .:mysql-connector-j-<version>.jar Main

# Run (Windows)
java -cp .;mysql-connector-j-<version>.jar Main
```

**If using an IDE (IntelliJ / Eclipse / VS Code):**
1. Create a new Java project, add all `.java` files.
2. Add the MySQL connector jar to your project's classpath / libraries.
3. Run `Main.java`.

## Sample Run
```
===== SIMPLE BANKING SYSTEM =====
1. Create Account
2. Deposit Money
3. Withdraw Money
4. Check Balance
5. Transfer Funds
6. View All Accounts
7. View Transaction History
8. Exit
Enter your choice: 1
Enter Account Number: ACC001
Enter Name: Etta Vani
Enter Initial Deposit: 5000
Enter Account Type (Savings/Current): Savings
Account created successfully!
Account No: ACC001 | Name: Etta Vani | Type: Savings | Balance: Rs. 5000.00
```

## Concepts You Can Talk About in Your Assessment
- **OOP**: `Account` class uses encapsulation (private fields, getters/setters)
- **Exception Handling**: custom `InsufficientBalanceException`, try-catch-finally in `Main`
- **JDBC**: `Connection`, `PreparedStatement` (prevents SQL injection), `ResultSet`
- **Transactions**: `transfer()` uses `setAutoCommit(false)` + `commit()`/`rollback()` so a transfer either fully succeeds or fully fails — never leaves money "half moved"
- **Collections**: `ArrayList<Account>`, `ArrayList<String>` used to hold query results
