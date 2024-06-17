import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankY {
    // Account model
    public static class Account {
        private String accountNumber;
        private String accountHolder;
        private double balance;

        public Account(String accountNumber, String accountHolder, double balance) {
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.balance = balance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountHolder() {
            return accountHolder;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public void withdraw(double amount) throws IllegalArgumentException {
            if (amount > balance) {
                throw new IllegalArgumentException("Insufficient funds.");
            }
            balance -= amount;
        }

        public void transfer(Account toAccount, double amount) {
            this.withdraw(amount);
            toAccount.deposit(amount);
        }
    }

    // Bank class to manage accounts
    public static class Bank {
        private Map<String, Account> accounts = new HashMap<>();
        private FileHandler fileHandler = new FileHandler();

        public void addAccount(Account account) {
            accounts.put(account.getAccountNumber(), account);
        }

        public Account getAccount(String accountNumber) {
            return accounts.get(accountNumber);
        }

        public void loadAccounts() {
            accounts = fileHandler.loadAccountsFromFile();
        }

        public void saveAccounts() {
            fileHandler.saveAccountsToFile(accounts);
        }
    }

    // Handles file operations
    public static class FileHandler {
        private static final String FILE_NAME = "accounts.txt";

        public Map<String, Account> loadAccountsFromFile() {
            Map<String, Account> accounts = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    String accountNumber = parts[0];
                    String accountHolder = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    accounts.put(accountNumber, new Account(accountNumber, accountHolder, balance));
                }
            } catch (IOException e) {
                System.out.println("Error loading accounts from file.");
            }
            return accounts;
        }

        public void saveAccountsToFile(Map<String, Account> accounts) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Account account : accounts.values()) {
                    bw.write(account.getAccountNumber() + "," + account.getAccountHolder() + "," + account.getBalance());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving accounts to file.");
            }
        }
    }

    // Handles account-related actions
    public static class AccountController {
        private Bank bank;
        private Scanner scanner = new Scanner(System.in);

        public AccountController(Bank bank) {
            this.bank = bank;
        }

        public void showMenu() {
            while (true) {
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        transfer();
                        break;
                    case 5:
                        bank.saveAccounts();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private void createAccount() {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Enter account holder name: ");
            String accountHolder = scanner.nextLine();
            System.out.print("Enter initial balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Account account = new Account(accountNumber, accountHolder, balance);
            bank.addAccount(account);

            System.out.println("Account created successfully.");
        }

        private void deposit() {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            Account account = bank.getAccount(accountNumber);

            if (account != null) {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                account.deposit(amount);
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Account not found.");
            }
        }

        private void withdraw() {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine();
            Account account = bank.getAccount(accountNumber);

            if (account != null) {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                try {
                    account.withdraw(amount);
                    System.out.println("Withdrawal successful.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Account not found.");
            }
        }

        private void transfer() {
            System.out.print("Enter source account number: ");
            String sourceAccountNumber = scanner.nextLine();
            Account sourceAccount = bank.getAccount(sourceAccountNumber);

            if (sourceAccount != null) {
                System.out.print("Enter destination account number: ");
                String destinationAccountNumber = scanner.nextLine();
                Account destinationAccount = bank.getAccount(destinationAccountNumber);

                if (destinationAccount != null) {
                    System.out.print("Enter amount to transfer: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    try {
                        sourceAccount.transfer(destinationAccount, amount);
                        System.out.println("Transfer successful.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Destination account not found.");
                }
            } else {
                System.out.println("Source account not found.");
            }
        }
    }

    // Entry point for the application
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.loadAccounts(); // Load accounts from file

        AccountController accountController = new AccountController(bank);
        accountController.showMenu();
    }
}

