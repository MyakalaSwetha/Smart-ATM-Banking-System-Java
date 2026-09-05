package com.atm.system;



	import java.io.*;
	import java.text.SimpleDateFormat;
	import java.util.*;

	// Custom Exception for Business Logic
	class InsufficientBalanceException extends Exception {
	    public InsufficientBalanceException(String message) {
	        super(message);
	    }
	}

	// Bank Account Model demonstrating Encapsulation
	class BankAccount {
	    private String accountNumber;
	    private String accountHolderName;
	    private double balance;
	    private List<String> transactionHistory;

	    public BankAccount(String accountNumber, String accountHolderName, double initialDeposit) {
	        this.accountNumber = accountNumber;
	        this.accountHolderName = accountHolderName;
	        this.balance = initialDeposit;
	        this.transactionHistory = new ArrayList<>();
	        addTransaction("Account Created with Initial Balance: ₹" + initialDeposit);
	    }

	    public String getAccountNumber() { return accountNumber; }
	    public String getAccountHolderName() { return accountHolderName; }
	    public double getBalance() { return balance; }

	    public void deposit(double amount) {
	        if (amount > 0) {
	            balance += amount;
	            addTransaction("Deposited: ₹" + amount + " | Total Balance: ₹" + balance);
	            System.out.println("✅ Successfully deposited ₹" + amount);
	        } else {
	            System.out.println("❌ Invalid deposit amount!");
	        }
	    }

	    public void withdraw(double amount) throws InsufficientBalanceException {
	        if (amount <= 0) {
	            System.out.println("❌ Invalid withdrawal amount!");
	            return;
	        }
	        if (amount > balance) {
	            throw new InsufficientBalanceException("❌ Insufficient Funds! Current Balance: ₹" + balance);
	        }
	        balance -= amount;
	        addTransaction("Withdrew: ₹" + amount + " | Remaining Balance: ₹" + balance);
	        System.out.println("✅ Successfully withdrew ₹" + amount);
	    }

	    private void addTransaction(String detail) {
	        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        transactionHistory.add("[" + timeStamp + "] " + detail);
	    }

	    public void printStatement() {
	        System.out.println("\n--- Passbook / Mini Statement ---");
	        for (String record : transactionHistory) {
	            System.out.println(record);
	        }
	        System.out.println("Current Balance: ₹" + balance);
	        System.out.println("---------------------------------");
	    }

	    public void exportStatementToFile() {
	        String fileName = accountNumber + "_Statement.txt";
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	            writer.write("=== BANK STATEMENT: " + accountHolderName + " (" + accountNumber + ") ===\n");
	            for (String record : transactionHistory) {
	                writer.write(record + "\n");
	            }
	            writer.write("Final Balance: ₹" + balance + "\n");
	            System.out.println("📄 Statement downloaded successfully as " + fileName);
	        } catch (IOException e) {
	            System.out.println("❌ Error saving file: " + e.getMessage());
	        }
	    }
	}

	// Main Driver Application
	public class SmartATMSystem {
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        Map<String, BankAccount> accountsDB = new HashMap<>();

	        BankAccount demoAcc = new BankAccount("ACC1001", "Developer Candidate", 5000.0);
	        accountsDB.put(demoAcc.getAccountNumber(), demoAcc);

	        System.out.println("==========================================");
	        System.out.println("   WELCOME TO SMART CONSOLE BANKING APPS   ");
	        System.out.println("==========================================");

	        while (true) {
	            System.out.println("\n1. Create New Account");
	            System.out.println("2. Deposit Money");
	            System.out.println("3. Withdraw Money");
	            System.out.println("4. Check Mini-Statement");
	            System.out.println("5. Export Passbook to File (.txt)");
	            System.out.println("6. Exit");
	            System.out.print("Choose Option (1-6): ");

	            int choice = scanner.nextInt();
	            scanner.nextLine();

	            switch (choice) {
	                case 1:
	                    System.out.print("Enter Account Number: ");
	                    String accNum = scanner.nextLine();
	                    System.out.print("Enter Name: ");
	                    String name = scanner.nextLine();
	                    System.out.print("Enter Initial Balance: ");
	                    double initBal = scanner.nextDouble();
	                    accountsDB.put(accNum, new BankAccount(accNum, name, initBal));
	                    System.out.println("🎉 Account Created Successfully!");
	                    break;

	                case 2:
	                    System.out.print("Enter Account Number: ");
	                    accNum = scanner.nextLine();
	                    if (accountsDB.containsKey(accNum)) {
	                        System.out.print("Enter Deposit Amount: ");
	                        double amt = scanner.nextDouble();
	                        accountsDB.get(accNum).deposit(amt);
	                    } else {
	                        System.out.println("❌ Account not found!");
	                    }
	                    break;

	                case 3:
	                    System.out.print("Enter Account Number: ");
	                    accNum = scanner.nextLine();
	                    if (accountsDB.containsKey(accNum)) {
	                        System.out.print("Enter Withdrawal Amount: ");
	                        double amt = scanner.nextDouble();
	                        try {
	                            accountsDB.get(accNum).withdraw(amt);
	                        } catch (InsufficientBalanceException e) {
	                            System.out.println(e.getMessage());
	                        }
	                    } else {
	                        System.out.println("❌ Account not found!");
	                    }
	                    break;

	                case 4:
	                    System.out.print("Enter Account Number: ");
	                    accNum = scanner.nextLine();
	                    if (accountsDB.containsKey(accNum)) {
	                        accountsDB.get(accNum).printStatement();
	                    } else {
	                        System.out.println("❌ Account not found!");
	                    }
	                    break;

	                case 5:
	                    System.out.print("Enter Account Number: ");
	                    accNum = scanner.nextLine();
	                    if (accountsDB.containsKey(accNum)) {
	                        accountsDB.get(accNum).exportStatementToFile();
	                    } else {
	                        System.out.println("❌ Account not found!");
	                    }
	                    break;

	                case 6:
	                    System.out.println("Thank you for using Smart Banking App. Goodbye!");
	                    scanner.close();
	                    System.exit(0);

	                default:
	                    System.out.println("Invalid option! Try again.");
	            }
	        }
	    }
	}

