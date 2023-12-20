package project;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private List<String> transactionHistory;
    private double interestRate;
    private Date lastTransactionDate;

    public BankAccount(String accountNumber, String accountHolder, double balance, double interestRate) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.interestRate = interestRate;
        this.lastTransactionDate = new Date();
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            recordTransaction("Deposit", amount);
            lastTransactionDate = new Date();
            System.out.println("Deposit successful. New balance: $" + balance);
        } 
        else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recordTransaction("Withdrawal", amount);
            lastTransactionDate = new Date();
            System.out.println("Withdrawal successful. New balance: $" + balance);
            return true;
        } 
        else {
            System.out.println("Insufficient balance or invalid withdrawal amount.");
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getInterestRate() {
		return interestRate;
	}

	public void calculateInterest() {
        
    }

    public abstract void displayAccountType();

    public void displayTransactionHistory() {
        System.out.println("Transaction History for Account " + accountNumber + ":");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    private void recordTransaction(String transactionType, double amount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String transactionDate = dateFormat.format(lastTransactionDate);
        String transactionInfo = transactionDate + " - " + transactionType + ": $" + amount;
        transactionHistory.add(transactionInfo);
    }
}

class SavingsAccount extends BankAccount {
    public SavingsAccount(String accountNumber, String accountHolder, double balance, double interestRate) {
        super(accountNumber, accountHolder, balance, interestRate);
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * getInterestRate() / 100;
        deposit(interest);
        System.out.println("Interest calculated and deposited. New balance: $" + getBalance());
    }

    @Override
    public void displayAccountType() {
        System.out.println("Account Type: Savings Account");
    }
}

class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String accountHolder, double balance, double overdraftLimit) {
        super(accountNumber, accountHolder, balance, 0);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (getBalance() - amount) >= -overdraftLimit) {
            super.withdraw(amount);
            return true;
        } else {
            System.out.println("Withdrawal exceeds overdraft limit or invalid amount.");
            return false;
        }
    }

    @Override
    public void calculateInterest() {
        // Checking accounts typically don't earn interest, so this method is empty.
    }

    @Override
    public void displayAccountType() {
        System.out.println("Account Type: Checking Account");
    }
}

public class BankingSystem {
    private Map<String, BankAccount> accounts;

    public BankingSystem() {
        accounts = new HashMap<>();
    }

    public void createSavingsAccount(String accountNumber, String accountHolder, double initialBalance, double interestRate) {
        SavingsAccount account = new SavingsAccount(accountNumber, accountHolder, initialBalance, interestRate);
        accounts.put(accountNumber, account);
        System.out.println("Savings Account created successfully.");
    }

    public void createCheckingAccount(String accountNumber, String accountHolder, double initialBalance, double overdraftLimit) {
        CheckingAccount account = new CheckingAccount(accountNumber, accountHolder, initialBalance, overdraftLimit);
        accounts.put(accountNumber, account);
        System.out.println("Checking Account created successfully.");
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public static void main(String[] args) {
    	BankingSystem bank = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nBank Management System Menu:");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Checking Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Check Balance");
            System.out.println("6. Calculate Interest");
            System.out.println("7. Display Transaction History");
            System.out.println("8. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String savingsAccountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String savingsAccountHolder = scanner.nextLine();
                    System.out.print("Enter initial balance: $");
                    double savingsInitialBalance = scanner.nextDouble();
                    System.out.print("Enter interest rate (%): ");
                    double interestRate = scanner.nextDouble();
                    bank.createSavingsAccount(savingsAccountNumber, savingsAccountHolder, savingsInitialBalance, interestRate);
                    break;

                case 2:
                    System.out.print("Enter account number: ");
                    String checkingAccountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String checkingAccountHolder = scanner.nextLine();
                    System.out.print("Enter initial balance: $");
                    double checkingInitialBalance = scanner.nextDouble();
                    System.out.print("Enter overdraft limit: $");
                    double overdraftLimit = scanner.nextDouble();
                    bank.createCheckingAccount(checkingAccountNumber, checkingAccountHolder, checkingInitialBalance, overdraftLimit);
                    break;

                case 3:
                    System.out.print("Enter account number: ");
                    String depositAccountNumber = scanner.nextLine();
                    BankAccount depositAccount = bank.getAccount(depositAccountNumber);
                    if (depositAccount != null) {
                        System.out.print("Enter deposit amount: $");
                        double depositAmount = scanner.nextDouble();
                        depositAccount.deposit(depositAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter account number: ");
                    String withdrawAccountNumber = scanner.nextLine();
                    BankAccount withdrawAccount = bank.getAccount(withdrawAccountNumber);
                    if (withdrawAccount != null) {
                        System.out.print("Enter withdrawal amount: $");
                        double withdrawAmount = scanner.nextDouble();
                        withdrawAccount.withdraw(withdrawAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter account number: ");
                    String checkBalanceAccountNumber = scanner.nextLine();
                    BankAccount checkBalanceAccount = bank.getAccount(checkBalanceAccountNumber);
                    if (checkBalanceAccount != null) {
                        System.out.println("Account Holder: " + checkBalanceAccount.getAccountHolder());
                        System.out.println("Account Number: " + checkBalanceAccount.getAccountNumber());
                        System.out.println("Balance: $" + checkBalanceAccount.getBalance());
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 6:
                    System.out.print("Enter account number: ");
                    String interestAccountNumber = scanner.nextLine();
                    BankAccount interestAccount = bank.getAccount(interestAccountNumber);
                    if (interestAccount != null && interestAccount instanceof SavingsAccount) {
                        ((SavingsAccount) interestAccount).calculateInterest();
                    } else {
                        System.out.println("Savings Account not found.");
                    }
                    break;

                case 7:
                    System.out.print("Enter account number: ");
                    String historyAccountNumber = scanner.nextLine();
                    BankAccount historyAccount = bank.getAccount(historyAccountNumber);
                    if (historyAccount != null) {
                        historyAccount.displayTransactionHistory();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 8:
                    System.out.println("Thank you for using the Bank Management System.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}