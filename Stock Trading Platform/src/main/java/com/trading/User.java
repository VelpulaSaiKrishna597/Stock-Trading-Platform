package com.trading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User class representing a trading platform user.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String name;
    private double balance;
    private double initialBalance;
    private List<Transaction> transactions;

    /**
     * Initialize a user.
     *
     * @param userId Unique user identifier
     * @param name User's name
     * @param initialBalance Starting cash balance
     */
    public User(String userId, String name, double initialBalance) {
        this.userId = userId;
        this.name = name;
        this.balance = initialBalance;
        this.initialBalance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    /**
     * Add a transaction to user's history.
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Get all transactions for this user.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    /**
     * Calculate total amount invested in stocks.
     */
    public double getTotalInvested() {
        double total = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType() == TransactionType.BUY) {
                total += transaction.getTotalValue();
            } else if (transaction.getTransactionType() == TransactionType.SELL) {
                total -= transaction.getTotalValue();
            }
        }
        return total;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
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

    public double getInitialBalance() {
        return initialBalance;
    }

    @Override
    public String toString() {
        return String.format("User(%s, %s, Balance: $%.2f)", userId, name, balance);
    }
}

