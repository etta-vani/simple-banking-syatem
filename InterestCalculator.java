/**
 * InterestCalculator.java
 * Utility class for calculating interest on Savings accounts.
 * Kept separate from Account/BankDAO to show separation of concerns —
 * business logic isolated from data model and data access.
 */
public class InterestCalculator {

    // Simple annual interest rate for Savings accounts (4%)
    private static final double SAVINGS_INTEREST_RATE = 0.04;

    /**
     * Calculates simple interest for a given number of years.
     * Formula: SI = (P * R * T)
     */
    public static double calculateInterest(double principal, int years) {
        if (principal <= 0 || years <= 0) {
            return 0.0;
        }
        return principal * SAVINGS_INTEREST_RATE * years;
    }

    public static double getInterestRate() {
        return SAVINGS_INTEREST_RATE;
    }
}
