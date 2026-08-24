import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * StatementExporter.java
 * Handles exporting a mini bank statement to a .txt file using Java File I/O
 * (FileWriter). This is a commonly asked Java fundamentals topic — good to
 * be able to explain in an assessment/interview.
 */
public class StatementExporter {

    public static void exportStatement(Account account, List<String> transactions) throws IOException {
        String fileName = "Statement_" + account.getAccountNumber() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("========================================\n");
            writer.write("           BANK STATEMENT\n");
            writer.write("========================================\n");
            writer.write("Account Number : " + account.getAccountNumber() + "\n");
            writer.write("Name           : " + account.getName() + "\n");
            writer.write("Account Type   : " + account.getAccountType() + "\n");
            writer.write("Current Balance: Rs. " + String.format("%.2f", account.getBalance()) + "\n");
            writer.write("----------------------------------------\n");
            writer.write("TRANSACTION HISTORY\n");
            writer.write("----------------------------------------\n");

            if (transactions.isEmpty()) {
                writer.write("No transactions found.\n");
            } else {
                for (String txn : transactions) {
                    writer.write(txn + "\n");
                }
            }

            writer.write("========================================\n");
        }

        System.out.println("Statement exported successfully to: " + fileName);
    }
}
