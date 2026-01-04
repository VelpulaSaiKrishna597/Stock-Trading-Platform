package com.trading;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transaction class representing buy/sell operations.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private TransactionType transactionType;
    private String stockSymbol;
    private int quantity;
    private double pricePerShare;
    private double totalValue;
    private String userId;
    private LocalDateTime timestamp;

    /**
     * Initialize a transaction.
     *
     * @param transactionType BUY or SELL
     * @param stockSymbol Stock ticker symbol
     * @param quantity Number of shares
     * @param pricePerShare Price at time of transaction
     * @param userId ID of user making transaction
     * @param transactionId Optional unique transaction ID
     */
    public Transaction(TransactionType transactionType, String stockSymbol, int quantity,
                      double pricePerShare, String userId, String transactionId) {
        this.transactionType = transactionType;
        this.stockSymbol = stockSymbol.toUpperCase();
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.totalValue = quantity * pricePerShare;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.transactionId = transactionId != null ? transactionId :
                String.format("%d_%s", System.currentTimeMillis(), stockSymbol);
    }

    public Transaction(TransactionType transactionType, String stockSymbol, int quantity,
                      double pricePerShare, String userId) {
        this(transactionType, stockSymbol, quantity, pricePerShare, userId, null);
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s %d shares of %s @ $%.2f = $%.2f",
                transactionType, quantity, stockSymbol, pricePerShare, totalValue);
    }
}

